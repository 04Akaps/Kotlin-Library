import advice.TxAdvice
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionSynchronizationManager
import kotlin.test.Test
import kotlin.test.assertTrue
import org.springframework.transaction.support.AbstractPlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionStatus
import kotlin.test.assertFalse

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [TestConfig::class])
open class TxAdviceTest {

    @Autowired
    lateinit var txAdvice: TxAdvice

    @Test
    fun `run() 메서드가 새로운 트랜잭션을 시작하는지 확인`() {
        txAdvice.run {
            assertTrue(TransactionSynchronizationManager.isActualTransactionActive(), "트랜잭션이 활성화되지 않음")
        }

        assertFalse(TransactionSynchronizationManager.isActualTransactionActive(), "트랜잭션이 활성화 되어 있음")
    }

    @Test
    fun `예외 발생 시 트랜잭션이 롤백되는지 확인`() {
        assertThrows<RuntimeException> {
            txAdvice.run {
                assertTrue(TransactionSynchronizationManager.isActualTransactionActive(), "트랜잭션이 활성화되지 않음")
                throw RuntimeException("예외 발생!")
            }
        }

        assertFalse(TransactionSynchronizationManager.isActualTransactionActive(), "트랜잭션이 종료되지 않음")
    }

    @Test
    fun `새로운 트랜잭션이 실행되는지 확인`() {
        txAdvice.run {
            assertTrue(TransactionSynchronizationManager.isActualTransactionActive(), "Outer 트랜잭션이 활성화되지 않음")
            println("Outer 트랜잭션 활성화 여부: ${TransactionSynchronizationManager.isActualTransactionActive()}")
        }

        assertFalse(TransactionSynchronizationManager.isActualTransactionActive(), "트랜잭션 일시 중단")

        txAdvice.run {
            assertTrue(TransactionSynchronizationManager.isActualTransactionActive(), "Inner 트랜잭션이 활성화되지 않음")
            println("Inner 트랜잭션 활성화 여부: ${TransactionSynchronizationManager.isActualTransactionActive()}")
        }
    }

    @Test
    fun `ReadOnly 트랜잭션이 정상적으로 설정되는지 확인`() {
        txAdvice.readOnly {
            assertTrue(TransactionSynchronizationManager.isActualTransactionActive(), "트랜잭션이 활성화되지 않음")
            assertTrue(TransactionSynchronizationManager.isCurrentTransactionReadOnly(), "트랜잭션이 ReadOnly 모드가 아님")
        }

        // 트랜잭션 종료 후 확인
        assertFalse(TransactionSynchronizationManager.isActualTransactionActive(), "트랜잭션이 종료되지 않음")
    }

    @Test
    fun `Propagation REQUIRES_NEW로 새로운 트랜잭션이 실행되는지 확인`() {
        txAdvice.run {
            val outerActive = TransactionSynchronizationManager.isActualTransactionActive()
            println("Outer 트랜잭션 활성화 여부: $outerActive")

            txAdvice.runNewTransaction {
                val innerActive = TransactionSynchronizationManager.isActualTransactionActive()
                println("Inner 트랜잭션 활성화 여부 (REQUIRES_NEW): $innerActive")

                // 내부 트랜잭션이 별도로 실행되므로 활성화 상태여야 함
                assertTrue(innerActive, "새로운 트랜잭션이 실행되지 않음")
            }
        }
    }

}



@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = ["advice"])
class TestConfig {
    @Bean
    fun transactionManager(): PlatformTransactionManager {
        return object : AbstractPlatformTransactionManager() {
            override fun doGetTransaction(): Any {
                return Any()
            }
            override fun doBegin(transaction: Any, definition: org.springframework.transaction.TransactionDefinition) {}
            override fun doCommit(status: DefaultTransactionStatus) {}
            override fun doRollback(status: DefaultTransactionStatus) {}
        }
    }
}