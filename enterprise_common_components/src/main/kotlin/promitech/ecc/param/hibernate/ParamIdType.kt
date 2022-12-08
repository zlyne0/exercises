package promitech.ecc.param.hibernate

import promitech.ecc.param.ParamId
import org.hibernate.id.ResultSetIdentifierConsumer
import org.hibernate.type.AbstractSingleColumnStandardBasicType
import org.hibernate.type.descriptor.sql.BigIntTypeDescriptor
import java.io.Serializable
import java.sql.ResultSet
import java.sql.SQLException

class ParamIdType :
    AbstractSingleColumnStandardBasicType<ParamId>(
        BigIntTypeDescriptor.INSTANCE,
        ParamIdTypeDescriptor.INSTANCE
    ),
    ResultSetIdentifierConsumer
{
    override fun consumeIdentifier(resultSet: ResultSet): Serializable {
        try {
            val id = resultSet.getLong(1)
            return javaTypeDescriptor.wrap(id, null)
        } catch (ex: SQLException) {
            throw IllegalStateException("Could not extract ID from ResultSet", ex)
        }
    }

    override fun getName(): String {
        return javaTypeDescriptor.javaType.simpleName
    }
}