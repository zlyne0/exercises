package promitech.ecc.outbox

import promitech.ecc.blob.json.JsonObjectId
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "ecc_outbox")
class OutboxEntity(

    @EmbeddedId
    @AttributeOverride(name = "value", column = Column(name = "id"))
    val id: OutboxEntityId,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "json_id"))
    val jsonObjectId: JsonObjectId,

    @Column(name = "type")
    val type: String,

    @Column(name = "error_count")
    var errorCount: Int = 0,

    @Column(name = "last_error_date")
    var lastErrorDate: LocalDateTime? = null
) {
    fun increaseErrorCount(date: LocalDateTime) {
        errorCount++
        lastErrorDate = date
    }
}
