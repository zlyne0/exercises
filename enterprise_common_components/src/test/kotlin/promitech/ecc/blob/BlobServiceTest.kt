package promitech.ecc.blob

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import promitech.ecc.TransactionService

@DataJpaTest
@EnableAutoConfiguration(exclude = [AutoConfigureTestDatabase::class])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableJpaAuditing
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ContextConfiguration(classes = [BlobRepository::class, BlobService::class, TransactionService::class])
@TestPropertySource(
    locations = ["classpath:test.properties"],
    properties = ["spring.liquibase.change-log=classpath:db.test.changelog/blob_test.xml"]
)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class BlobServiceTest {

    @Autowired
    private lateinit var blobService: BlobService

    @Autowired
    private lateinit var transactionService: TransactionService

    @Test
    fun `should save and load blob content`() {
        // given
        val blobContent = BlobContent("content example".toByteArray())

        // when
        val blobId: BlobId = transactionService.runInNewTransaction {
            blobService.save(blobContent)
        }

        val loadedBlobContent = transactionService.runInNewTransaction {
            blobService.find(blobId)
        }

        // then
        assertThat(loadedBlobContent).isEqualTo(blobContent)
    }

    @Test
    fun `should update value`() {
        // given
        val blobContent = BlobContent("content example".toByteArray())

        val blobId: BlobId = transactionService.runInNewTransaction {
            blobService.save(blobContent)
        }

        val newContent = BlobContent("new content".toByteArray())

        // when
        transactionService.runInNewTransaction {
            blobService.update(blobId, newContent)
        }

        // then
        val updatedContent = transactionService.runInNewTransaction {
            blobService.find(blobId)
        }
        assertThat(updatedContent).isEqualTo(newContent)
    }
}