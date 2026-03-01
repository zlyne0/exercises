package demo

import demo.diagramdocumenter.MyDocumenter
import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules
import org.springframework.modulith.core.DependencyDepth
import org.springframework.modulith.docs.Documenter

class ModuleDependencyDocumenterTest {

//    @Test
    fun `should check bean dependencies`() {
        // given
        // when
        val modules = ApplicationModules.of(DemoApplication::class.java)

//        Documenter(modules)
//            .writeModulesAsPlantUml()
//            .writeIndividualModulesAsPlantUml()
//            .writeModuleCanvases()
//            .writeAggregatingDocument()

        val diagramOptions = Documenter.DiagramOptions.defaults()
            .withStyle(Documenter.DiagramOptions.DiagramStyle.C4)
            .withDependencyDepth(DependencyDepth.ALL)
        Documenter(modules)
//            .writeDocumentation()
//            .writeAggregatingDocument()
            .writeModulesAsPlantUml(diagramOptions)
            .writeIndividualModulesAsPlantUml()

        // then
        modules.verify()
    }

    @Test
    fun `generate complete C4 component view`() {
        // given
        val modules = ApplicationModules.of("demo")

        MyDocumenter(modules)
            .useOnlyAnnotatedModules()
            .writeModulesAsPlantUml()
            .writeOnlyRootModules()
            .writeModulesWithoutRelations()
    }
}