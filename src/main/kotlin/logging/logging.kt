package logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.system.measureTimeMillis

object Logging {
    fun <T : Any> getLogger(forClass: Class<T>): Logger = LoggerFactory.getLogger(forClass)

    fun <T> loggingStopWatch(logger: Logger, function: () -> T?): T? {
        val duration = measureTimeMillis {
            logger.info("Start At : ${System.currentTimeMillis()}")
        }

        val result = function.invoke()

        logger.info("End At : ${System.currentTimeMillis()}")
        logger.info("Logic Duration : ${duration}ms")

        return result
    }
}