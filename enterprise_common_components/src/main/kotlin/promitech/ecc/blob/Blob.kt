package promitech.ecc.blob

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.domain.Persistable
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "ecc_blob")
@EntityListeners(AuditingEntityListener::class)
class Blob(
    @EmbeddedId
    @AttributeOverride(name = "value", column = Column(name = "id"))
    val blobId: BlobId,

    @Column(name = "content")
    val blobContent: BlobContent,

    @Column(name = "version")
    @Version
    val version: Long = 0

): Persistable<BlobId> {

    @CreatedDate
    @Column(name = "INSERT_DATE_TIME")
    var insertDate: LocalDateTime? = null

    @LastModifiedDate
    @Column(name = "UPDATE_DATE_TIME")
    var updateDateTime: LocalDateTime? = null

    override fun getId(): BlobId? {
        return blobId
    }

    override fun isNew(): Boolean {
        return version == 0L
    }

}