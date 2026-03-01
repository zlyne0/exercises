package demo.diagramdocumenter

import com.structurizr.Workspace
import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.SoftwareSystem
import com.structurizr.model.Tags
import com.structurizr.view.Shape
import org.springframework.modulith.core.ApplicationModule
import org.springframework.modulith.core.ApplicationModules
import java.io.File
import java.util.UUID
import java.util.function.Predicate

class MyDocumenter(
    private val modules: ApplicationModules,
) {
    private val workspace: Workspace
    private val system: SoftwareSystem

    private var globalModuleFilters : Predicate<ApplicationModule> = allModules

    init {
        workspace = Workspace("Modulith", "")
        workspace.getViews().getConfiguration()
            .getStyles()
            .addElementStyle(Tags.COMPONENT)
            .shape(Shape.Component)


        val workspaceModel: Model = workspace.getModel()
        val systemName = getDefaultedSystemName()

        system = workspaceModel.addSoftwareSystem(systemName, "")
    }

    private fun getDefaultedSystemName(): String {
        return modules.getSystemName().orElse("Modules")
    }

    fun useOnlyAnnotatedModules(): MyDocumenter {
        globalModuleFilters = packagesAnnotateByApplicationModulePredicate
        return this
    }

//    fun withExcludePredicate(excludePredicate: Predicate<ApplicationModule>): MyDocumenter {
//        this.excludeModules = excludePredicate
//        return this
//    }

    fun writeModulesAsPlantUml(): MyDocumenter {
        val model = DependencyModel()
        model.componentByModule = createComponents()
        model.createModulesDependencies(modules)
        model.markSubmodules(modules)
        model.createRelationships(modules)

        val exporter = MyPlantUMLExporter(model)
        File("target/c4-components.puml").writeText(exporter.generateString())
        return this
    }

    fun writeOnlyRootModules(): MyDocumenter {
        val model = DependencyModel()
        val rootModulesFilter = Predicate<ApplicationModule> { module -> !modules.hasParent(module) }
        model.componentByModule = createComponents(rootModulesFilter)
        model.createRootsModulesDependencies(modules)
        model.createRelationships(modules)

        val exporter = MyPlantUMLExporter(model)
        File("target/c4-components-roots-only.puml").writeText(exporter.generateString())
        return this
    }

    fun writeModulesWithoutRelations(): MyDocumenter {
        val model = DependencyModel()
        model.componentByModule = createComponents()
        model.createModulesDependencies(modules)
        model.markSubmodules(modules)

        val exporter = MyPlantUMLExporter(model)
        File("target/c4-components-without-relations.puml").writeText(exporter.generateString())
        return this
    }

    private fun createComponents(moduleFilter: Predicate<ApplicationModule> = allModules): MutableMap<ApplicationModule, Component> {
        val container: Container = system.addContainer(UUID.randomUUID().toString(), "", "")

        val componentByModule = mutableMapOf<ApplicationModule, Component>()
        modules.stream()
            .filter(moduleFilter)
            .filter(globalModuleFilters)
            .forEach { module ->
                componentByModule.put(module, container.addComponent(module.displayName, "", "Module"))
            }
        return componentByModule
    }

    companion object {
        val packagesAnnotateByApplicationModulePredicate = Predicate<ApplicationModule> { module ->
            module.basePackage.getAnnotation(org.springframework.modulith.ApplicationModule::class.java).isPresent
        }

        val allModules = Predicate<ApplicationModule> { true }
    }
}