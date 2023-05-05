package promitech.ecc.outbox

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class OutboxEntityId(
    @Column(name = "blob_id")
    val value: Long
): Serializable {
    companion object {
        private const val serialVersionUID: Long = 1
    }
}