package promitech.ecc

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class TransactionService {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun <T> runInNewTransaction(action: () -> T ): T {
        return action.invoke()
    }

}