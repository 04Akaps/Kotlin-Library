package kotlin.library.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Logging {
    fun <T : Any> getLogger(forClass: Class<T>): Logger = LoggerFactory.getLogger(forClass)

    fun <T> loggingStopWatch(logger: Logger, function: (MutableMap<String, Any>) -> T?): T? {
        val logData = mutableMapOf<String, Any>()
        logData["startAt"] = System.currentTimeMillis()

        val result = function.invoke(logData)

        logData["endAt"] = System.currentTimeMillis()
        logger.info(logData.toString())

        return result
    }
}