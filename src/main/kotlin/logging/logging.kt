package logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import kotlin.system.measureTimeMillis

object Logging {
    fun <T : Any> getLogger(forClass: Class<T>): Logger = LoggerFactory.getLogger(forClass)

    fun <T> loggingStopWatch(logger: Logger, function: (MutableMap<String, Any>) -> T?): T? {
        val logData = mutableMapOf<String, Any>()
        logData["startAt"] = LocalDateTime.now()

        val result = function.invoke(logData)

        logData["endAt"] = LocalDateTime.now()
        logger.info(logData.toString())

        return result
    }
}