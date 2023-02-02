package promitech.ecc.blob

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import promitech.ecc.BaseITTest
import promitech.ecc.TransactionService

@ContextConfiguration(classes = [BlobConfiguration::class, TransactionService::class])
@TestPropertySource(
    properties = ["spring.liquibase.change-log=classpath:db.test.changelog/blob_test.xml"]
)
class BlobServiceTest: BaseITTest() {

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