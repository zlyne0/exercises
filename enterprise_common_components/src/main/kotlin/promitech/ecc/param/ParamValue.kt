package promitech.ecc.param

import com.fasterxml.jackson.annotation.JsonValue
import javax.persistence.AttributeConverter
import javax.persistence.Converter

data class ParamValue(@JsonValue val value: String) {
}

@Converter(autoApply = true)
class ParamValueConverter : AttributeConverter<ParamValue, String> {
    override fun convertToDatabaseColumn(attribute: ParamValue?): String? {
        if (attribute == null) {
            return null
        }
        return attribute.value
    }

    override fun convertToEntityAttribute(dbData: String?): ParamValue? {
        if (dbData == null) {
            return null
        }
        return ParamValue(dbData)
    }

}