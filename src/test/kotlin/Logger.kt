
import logging.Logging
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.springframework.stereotype.Service

class LoggingTest {

    private val testService: TestLoggerTest = TestLoggerTest()

    @Test
    fun `loggingStopWatch should log execution time and return function result`() {
        testService.onlyLogging()
    }

    @Test
    fun `loggingStopWatch can accept input custom logging`() {
        val userID : String = "test user ID"
        testService.loggingInput(userID)
    }
}


@Service
class TestLoggerTest() {
    private lateinit var mockLogger: Logger

    init {
        mockLogger = Logging.getLogger(TestLoggerTest::class.java)
    }

    fun onlyLogging() = Logging.loggingStopWatch(mockLogger) {
        println("only logging")
    }

    fun loggingInput(userID : String) = Logging.loggingStopWatch(mockLogger) { input ->
        input.put("userID", userID)
    }


}