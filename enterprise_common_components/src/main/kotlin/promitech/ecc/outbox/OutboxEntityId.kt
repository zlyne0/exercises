package promitech.ecc.outbox

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class OutboxEntityId(
    @Column(name = "blob_id")
    val value: Long
)