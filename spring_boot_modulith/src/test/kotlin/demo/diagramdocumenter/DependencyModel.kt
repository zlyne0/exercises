package demo.diagramdocumenter

import com.structurizr.model.Component
import com.structurizr.model.Relationship
import org.springframework.modulith.core.ApplicationModule
import org.springframework.modulith.core.ApplicationModuleDependencies
import org.springframework.modulith.core.ApplicationModuleDependency
import org.springframework.modulith.core.ApplicationModuleIdentifier
import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.core.DependencyType
import kotlin.collections.iterator

class ModuleGroup(
    val root: ApplicationModule,
) {
    val elements: MutableList<ModuleGroup> = mutableListOf()
    var withSubmodules: Boolean = false
}

class DependencyModel {
    val relationships = mutableListOf<Relationship>()

    var componentByModule = mutableMapOf<ApplicationModule, Component>()

    val moduleGroupPerApplicationModule = mutableMapOf<ApplicationModule, ModuleGroup>()
    val moduleGroups = mutableListOf<ModuleGroup>()

    fun addGroup(module: ApplicationModule) {
        if (!moduleGroupPerApplicationModule.contains(module)) {
            val mg = ModuleGroup(module)
            moduleGroupPerApplicationModule.put(module, mg)
            moduleGroups.add(mg)
        }
    }

    fun addGroup(parentModule: ApplicationModule, childModule: ApplicationModule) {
        var parentGroup = moduleGroupPerApplicationModule.get(parentModule)
        if (parentGroup == null) {
            parentGroup = ModuleGroup(parentModule)
            moduleGroupPerApplicationModule.put(parentModule, parentGroup)
            moduleGroups.add(parentGroup)
        }
        val childGroup = ModuleGroup(childModule)
        moduleGroupPerApplicationModule.put(childModule, childGroup)
        parentGroup.elements.add(childGroup)
    }

    fun addRelationship(relationship: Relationship) {
        relationships.add(relationship)
    }

    fun createModulesDependencies(modules: ApplicationModules) {
        val modulePool = mutableListOf<ApplicationModule>()
        for ((module, _) in componentByModule) {
            if (!modules.hasParent(module)) {
                modulePool.add(module)
            }
        }
        while (modulePool.isNotEmpty()) {
            val module = modulePool.removeFirst()
            addGroup(module)
            modules.getNestedModules(module).asSequence().forEach { nm ->
                val nmParent = modules.getParentOf(nm)
                if (nmParent.isPresent && nmParent.get() == module) {
                    modulePool.add(nm)
                    addGroup(module, nm)
                }
            }
        }
    }

    fun createRootsModulesDependencies(modules: ApplicationModules) {
        for ((module, _) in componentByModule) {
            if (!modules.hasParent(module)) {
                addGroup(module)
            }
        }
    }

    fun markSubmodules(modules: ApplicationModules) {
        for ((module, moduleGroup) in moduleGroupPerApplicationModule) {
            val hasParent = modules.hasParent(module)
            val hasNestedModules = modules.getNestedModules(module).isNotEmpty()
            if (!hasParent && hasNestedModules) {
                moduleGroup.withSubmodules = true
            }
            if (module.isOpen && hasNestedModules) {
                moduleGroup.withSubmodules = true
            }
        }
    }

    fun createRelationships(modules: ApplicationModules) {
        for ((appModule, component) in componentByModule) {
            appModule.getDirectDependencies(modules).moduleDependenciesSortByType().forEach { dep ->
                val destComponent = componentByModule.get(dep.targetModule)
                if (destComponent != null) {
                    val relationship = component.uses(destComponent, dependencyTypeName(dep.dependencyType))
                    relationship!!.addTags(dependencyTypeName(dep.dependencyType))
                    addRelationship(relationship)
                }
            }
        }
    }

    fun dependencyTypeName(type: DependencyType): String {
        return when(type) {
            DependencyType.USES_COMPONENT -> "uses"
            DependencyType.ENTITY -> "uses"
            DependencyType.EVENT_LISTENER -> "listens to"
            DependencyType.DEFAULT -> "depends on"
        }
    }

    private fun ApplicationModuleDependencies.moduleDependenciesSortByType(): Collection<ApplicationModuleDependency> {
        val map = mutableMapOf<ApplicationModuleIdentifier, ApplicationModuleDependency>()
        this.stream().forEach { module ->
            val tmpModule = map.get(module.targetModule.identifier)
            if (tmpModule != null) {
                if (tmpModule.dependencyType.ordinal > module.dependencyType.ordinal) {
                    map.put(module.targetModule.identifier, module)
                }
            } else {
                map.put(module.targetModule.identifier, module)
            }
        }
        return map.values
    }
}