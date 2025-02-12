import advice.TxAdvice
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

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [TestConfig::class])
class TxAdviceTest {

    @Autowired
    lateinit var txAdvice: TxAdvice

    @Test
    fun `run() 메서드가 새로운 트랜잭션을 시작하는지 확인`() {
        txAdvice.run {
            assertTrue(TransactionSynchronizationManager.isActualTransactionActive(), "트랜잭션이 활성화되지 않음")
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