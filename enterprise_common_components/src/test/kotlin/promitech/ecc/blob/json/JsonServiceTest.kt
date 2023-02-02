package promitech.ecc.blob.json

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import promitech.ecc.BaseITTest
import promitech.ecc.blob.BlobConfiguration
import java.time.LocalDate

data class Contract(
    val number: String,
    val startDate: LocalDate,
    val endDate: LocalDate
)

data class Agent(
    val firstName: String,
    val lastName: String,
    val contracts: List<Contract>
)

@ContextConfiguration(classes = [
    BlobConfiguration::class,
    JsonConfiguration::class
])
@TestPropertySource(
    properties = ["spring.liquibase.change-log=classpath:db.test.changelog/blob_test.xml"]
)
class JsonServiceTest : BaseITTest() {

    @Autowired
    private lateinit var sut: JsonService

    @Test
    fun `should save and load object`() {
        // given

        val agent = Agent(
            "Space", "Invader", listOf(
                Contract("0001", LocalDate.of(2020, 4, 17), LocalDate.of(2022, 7, 23)),
                Contract("0002", LocalDate.of(2025, 4, 17), LocalDate.of(2027, 7, 23))
            )
        )

        // when
        val jsonObjectId: JsonObjectId = sut.saveObject(agent)
        val persistedAgent = sut.loadObject(jsonObjectId, Agent::class.java)

        // then
        assertThat(persistedAgent).isEqualTo(agent)
    }
}