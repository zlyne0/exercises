package promitech.ecc.param

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource

@DataJpaTest
@TestPropertySource(properties = ["spring.liquibase.change-log=classpath:db.test.changelog/param_test.xml"])
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ParamRepositoryTests {

    @Autowired
    private lateinit var paramRepository: ParamRepository

    @Test
    fun `should find all params by name`() {
        // given
        val paramName1 = ParamName("param_name_1")
        val paramName2 = ParamName("param_name_2")
        val param1 = Param(name = paramName1,  value = ParamValue("param_value_1"))
        val param2 = Param(name = paramName2,  value = ParamValue("param_value_2"))

        paramRepository.save(param1)
        paramRepository.save(param2)

        // when
        val returnedParams = paramRepository.findAllBy(paramName1)

        // then
        assertThat(returnedParams).hasSize(1)
        assertThat(returnedParams.first().value).isEqualTo(param1.value)
    }

    @Test
    fun `should find by id`() {
        // given
        val paramName1 = ParamName("param_name_1")
        val paramName2 = ParamName("param_name_2")

        val savedParam1 = paramRepository.save(
            Param(name = paramName1,  value = ParamValue("param_value_1"))
        )
        paramRepository.save(
            Param(name = paramName2,  value = ParamValue("param_value_2"))
        )

        // when
        val foundParameter = paramRepository.getReferenceById(savedParam1.id!!)

        // then
        assertThat(foundParameter).isNotNull
        assertThat(foundParameter.name).isEqualTo(savedParam1.name)
        assertThat(foundParameter.value).isEqualTo(savedParam1.value)
    }
}