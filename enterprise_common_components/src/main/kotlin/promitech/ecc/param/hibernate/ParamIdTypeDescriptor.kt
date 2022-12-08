package promitech.ecc.param.hibernate

import promitech.ecc.param.ParamId
import org.hibernate.type.descriptor.WrapperOptions
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor
import org.hibernate.type.descriptor.java.LongTypeDescriptor

class ParamIdTypeDescriptor : AbstractTypeDescriptor<ParamId>(ParamId::class.java) {

    companion object {
        @JvmStatic
        val INSTANCE = ParamIdTypeDescriptor()
    }

    override fun toString(paramId: ParamId): String {
        return LongTypeDescriptor.INSTANCE.toString(paramId.value)
    }

    override fun fromString(string: String?): ParamId {
        return ParamId(LongTypeDescriptor.INSTANCE.fromString(string))
    }

    override fun <X : Any?> wrap(value: X, options: WrapperOptions): ParamId? {
        if (value == null) {
            return null
        }
        if (javaType.isInstance(value)) {
            return javaType.cast(value)
        }
        return ParamId(LongTypeDescriptor.INSTANCE.wrap(value, options))
    }

    override fun <X : Any?> unwrap(value: ParamId?, type: Class<X>?, options: WrapperOptions?): X? {
        if (value == null) {
            return null
        }
        return LongTypeDescriptor.INSTANCE.unwrap(value.value, type, options)
    }
}