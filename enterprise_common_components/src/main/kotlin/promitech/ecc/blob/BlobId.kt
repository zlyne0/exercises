package promitech.ecc.blob

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class BlobId(
    @Column(name = "blob_id")
    val value: Long
): Serializable
