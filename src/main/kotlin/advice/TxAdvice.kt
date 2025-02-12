package advice

import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.DefaultTransactionDefinition

@Component
class TxAdvice(
    private val txAdvice: Advice,
    private val transactionManagerMap: Map<String, PlatformTransactionManager> = emptyMap()
) {

    fun <T> run(function: () -> T?): T? {
        return txAdvice.run(function)
    }

    fun <T> readOnly(function: () -> T?): T? {
        return txAdvice.readOnly(function)
    }

    fun <T> runNewTransaction(function: () -> T?): T? {
        return txAdvice.runNewTransaction(function)
    }

    fun <T> runWithManager(manager : String, function: () -> T?): T? {
        return txAdvice.runWithManager(manager, transactionManagerMap, function)
    }

    @Component
    class Advice {

        @Transactional
        fun <T> run(function: () -> T?): T? {
            return function()
        }

        @Transactional(readOnly = true)
        fun <T> readOnly(function: () -> T?): T? {
            return function()
        }

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        fun <T> runNewTransaction(function: () -> T?): T? {
            return function()
        }

        fun <T> runWithManager(
            manager : String,
            transactionManagerMap: Map<String, PlatformTransactionManager>,
            function: () -> T?
        ): T? {
            val transactionManager = transactionManagerMap[manager]
                ?: throw IllegalArgumentException("Invalid transaction manager: $manager")

            val transactionDefinition = DefaultTransactionDefinition().apply {
                propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRES_NEW
            }
            val status = transactionManager.getTransaction(transactionDefinition)

            return try {
                val result = function()
                transactionManager.commit(status)
                result
            } catch (ex: Exception) {
                transactionManager.rollback(status)
                throw ex
            }
        }
    }
}