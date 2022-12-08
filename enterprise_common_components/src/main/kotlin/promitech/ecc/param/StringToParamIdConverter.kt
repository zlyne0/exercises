package promitech.ecc.param

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class StringToParamIdConverter : Converter<String, ParamId> {
    override fun convert(source: String): ParamId {
        return ParamId(source.toLong())
    }
}