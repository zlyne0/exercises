package promitech.ecc.param.hibernate

import promitech.ecc.param.ParamId
import org.hibernate.boot.model.relational.Database
import org.hibernate.boot.model.relational.SqlStringGenerationContext
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator
import org.hibernate.id.IntegralDataTypeHolder
import org.hibernate.id.enhanced.SequenceStyleGenerator
import org.hibernate.service.ServiceRegistry
import org.hibernate.type.LongType
import org.hibernate.type.Type
import java.io.Serializable
import java.util.*

class ParamIdGenerator : IdentifierGenerator {

    val sequenceStyleGenerator = SequenceStyleGenerator()

    override fun configure(type: Type, params: Properties, serviceRegistry: ServiceRegistry) {
        sequenceStyleGenerator.configure(LongType.INSTANCE, params, serviceRegistry)
    }

    override fun registerExportables(database: Database) {
        sequenceStyleGenerator.registerExportables(database)
    }

    override fun initialize(context: SqlStringGenerationContext) {
        sequenceStyleGenerator.initialize(context)
    }

    override fun generate(session: SharedSessionContractImplementor, `object`: Any?): Serializable {
        val value: IntegralDataTypeHolder = sequenceStyleGenerator.databaseStructure.buildCallback(session).nextValue
        return ParamId(value.makeValue().toLong())
    }

    companion object {
        const val GENERATOR = "promitech.ecc.param.hibernate.ParamIdGenerator"
    }
}