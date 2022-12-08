package promitech.ecc.param

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
internal class ParamService(
    val paramRepository: ParamRepository
) {

    @Transactional
    fun createOne(): Param {
        val savedParam = paramRepository.save(
            Param(
                name = ParamName("name_" + UUID.randomUUID().toString()),
                value = ParamValue("some value")
            )
        )
        return savedParam
    }

    @Transactional(readOnly = true)
    fun findAll(): List<Param> {
        return paramRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun findAll(paramName: ParamName): List<Param> {
        return paramRepository.findAllBy(paramName)
    }

    @Transactional(readOnly = true)
    fun findAll(paramId: ParamId): List<Param> {
        return paramRepository.findAllBy(paramId)
    }
}