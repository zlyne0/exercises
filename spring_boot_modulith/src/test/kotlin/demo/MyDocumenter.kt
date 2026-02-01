package demo

import com.structurizr.Workspace
import com.structurizr.export.Diagram
import com.structurizr.export.plantuml.C4PlantUMLExporter
import com.structurizr.model.Component
import com.structurizr.model.Container
import com.structurizr.model.Model
import com.structurizr.model.Tags
import com.structurizr.view.Shape
import org.springframework.modulith.core.ApplicationModule
import org.springframework.modulith.core.ApplicationModuleDependencies
import org.springframework.modulith.core.ApplicationModuleDependency
import org.springframework.modulith.core.ApplicationModuleIdentifier
import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.core.DependencyType
import java.io.File

class MyDocumenter(
    val modules: ApplicationModules,
) {
    private val workspace: Workspace
    private val container: Container

    init {
        workspace = Workspace("Modulith", "")
        workspace.getViews().getConfiguration()
            .getStyles()
            .addElementStyle(Tags.COMPONENT)
            .shape(Shape.Component)


        val model: Model = workspace.getModel()
        val systemName = getDefaultedSystemName()

        val system = model.addSoftwareSystem(systemName, "")
        container = system.addContainer(systemName, "", "")
    }

    private fun getDefaultedSystemName(): String {
        return modules.getSystemName().orElse("Modules")
    }

    fun writeModulesAsPlantUml() {
        createComponents()
        render()
    }

    fun createComponents() {
        val components = mutableMapOf<ApplicationModule, Component>()
        for (module in modules) {
            components.put(module, container.addComponent(module.displayName, "", "Module"))
        }
        for ((appModule, component) in components) {
            appModule.getDirectDependencies(modules).moduleDependenciesSortByType().forEach { dep ->
                val destComponent = components.get(dep.targetModule)
                if (destComponent != null) {
                    val relationship = component.uses(destComponent, dependencyTypeName(dep.dependencyType))
                    relationship!!.addTags(dependencyTypeName(dep.dependencyType))
                }
            }
        }
    }

    fun render() {
        val view = workspace.getViews()
            .createComponentView(container, "modules-", "")
        view.addDefaultElements()
        val c4PlantUmlExporter = C4PlantUMLExporter()
        val diagram: Diagram = c4PlantUmlExporter.export(view)
        File("target/c4-components.puml").writeText(diagram.definition)
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