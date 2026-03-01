package demo.diagramdocumenter

import com.structurizr.export.IndentingWriter
import com.structurizr.model.Component
import com.structurizr.model.Element
import com.structurizr.model.ModelItem
import com.structurizr.model.Relationship
import org.springframework.modulith.core.ApplicationModule

class MyPlantUMLExporter(
    private val model: DependencyModel
) {
    private val writer = IndentingWriter()
    private var rectangleCounter: Int = 0

    fun generateString(): String {
        writeComponents()
        writeRelationships()
        return writeHeader() + writer.toString() + writeFooter()
    }

    fun writeComponents() {
        writer.writeLine()
        for (mg in model.moduleGroups) {
            writeGroupComponents(mg)
        }
    }

    fun writeGroupComponents(group: ModuleGroup) {
        if (group.withSubmodules) {
            writeAsRectangle(group.root) {
                writeComponent(model.componentByModule.get(group.root)!!)
                for (component in group.elements) {
                    writeGroupComponents(component)
                }
            }
        } else {
            writeComponent(model.componentByModule.get(group.root)!!)
            for (component in group.elements) {
                writeGroupComponents(component)
            }
        }
    }

    fun writeAsRectangle(module: ApplicationModule, action: () -> Unit) {
        val component = model.componentByModule.get(module)
        val id = idOf(component!!) + "_submodules"

        rectangleCounter++
        writer.writeLine("rectangle \"_\" as \"$id\" #line.dashed {")
        writer.indent()

        action()

        writer.outdent()
        writer.writeLine("}")
    }

    fun writeComponent(component: Component) {
        val id = idOf(component)
        val name = component.getName()
        val technology = component.technology ?: ""
        writer.writeLine(
            String.format(
                "Component(%s, \"%s\", \$techn=\"%s\", \$descr=\"\", \$tags=\"\", \$link=\"\")",
                id, name, technology
            )
        )
    }

    fun writeRelationships() {
        writer.writeLine()
        for (relationship in model.relationships) {
            writeRelationShip(relationship)
        }
        writer.writeLine()
    }

    fun writeRelationShip(relationship: Relationship) {
        val source = relationship.getSource()
        val destination = relationship.getDestination()
        val description = relationship.getDescription()
        val technology = relationship.technology ?: ""
        writer.writeLine(
            String.format(
                "Rel(%s, %s, \"%s\", \$techn=\"%s\", \$tags=\"\", \$link=\"\")",
                idOf(source), idOf(destination), description, technology
            )
        )
    }

    fun idOf(modelItem: ModelItem): String {
        if (modelItem is Element) {
            if (modelItem.parent == null) {
                return id(modelItem)
            } else {
                return idOf(modelItem.parent) + "." + id(modelItem)
            }
        }
        return id(modelItem)
    }

    fun id(modelItem: ModelItem): String {
        if (modelItem is Component) {
            return normalizeId(modelItem.name)
        }
        return modelItem.id
    }

    fun normalizeId(id: String): String {
        return id.replace("(?U)\\W".toRegex(), "")
    }

    fun writeHeader(): String {
        return """@startuml
title <size:24>Component View:</size>

set separator none
top to bottom direction

<style>
  root {
    BackgroundColor: #ffffff
    FontColor: #444444
  }
</style>

!include <C4/C4>
!include <C4/C4_Context>
!include <C4/C4_Component>
"""
    }

    fun writeFooter(): String {
        return """
'SHOW_LEGEND(false)
hide stereotypes
@enduml            
        """
    }
}