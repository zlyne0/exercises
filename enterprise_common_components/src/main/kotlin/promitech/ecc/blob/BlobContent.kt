package promitech.ecc.blob

import javax.persistence.AttributeConverter
import javax.persistence.Converter


data class BlobContent(val bytes: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BlobContent

        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode()
    }
}

@Converter(autoApply = true)
class BlobContentConverter : AttributeConverter<BlobContent, ByteArray> {
    override fun convertToDatabaseColumn(attribute: BlobContent?): ByteArray? {
        if (attribute == null) {
            return null
        }
        return attribute.bytes
    }

    override fun convertToEntityAttribute(dbData: ByteArray?): BlobContent? {
        if (dbData == null) {
            return null
        }
        return BlobContent(dbData)
    }

}
