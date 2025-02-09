package advice

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class TxAdvice(private val txAdvice: Advice) {

    fun <T> run(function: () -> T): T {
        return txAdvice.run(function)
    }

    fun <T> readOnly(function: () -> T): T {
        return txAdvice.readOnly(function)
    }

    @Component
    class Advice {

        @Transactional
        fun <T> run(function: () -> T): T {
            return function()
        }

        @Transactional(readOnly = true)
        fun <T> readOnly(function: () -> T): T {
            return function()
        }
    }
}