package promitech.ecc.param

import com.fasterxml.jackson.annotation.JsonValue
import javax.persistence.AttributeConverter
import javax.persistence.Converter

data class ParamName(@JsonValue val value: String)

@Converter(autoApply = true)
class ParamNameConverter : AttributeConverter<ParamName, String> {
    override fun convertToDatabaseColumn(attribute: ParamName?): String? {
        if (attribute == null) {
            return null
        }
        return attribute.value
    }

    override fun convertToEntityAttribute(dbData: String?): ParamName? {
        if (dbData == null) {
            return null
        }
        return ParamName(dbData)
    }

}