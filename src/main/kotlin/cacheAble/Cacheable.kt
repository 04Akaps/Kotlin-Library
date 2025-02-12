package cacheAble

import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component


@Component
class Cacheable (
    private val advice: CacheAdvice,
) {
    private val TOKEN: String = "::"

    fun <T> cache(vararg keys: Any, function: () -> T): T {
        return advice.cache(generateKey(keys), function)
    }

    fun <T> evict(vararg keys: Any, function: () -> T): T {
        return advice.evict(generateKey(keys), function)
    }

    private fun generateKey(keys: Array<out Any>) = keys.joinToString(TOKEN)

}

@Component
class CacheAdvice {
    companion object {
        private const val CACHE_NAME = "default:key"
    }

    @Cacheable(value = [CACHE_NAME], key = "#key")
    fun <T> cache(key: String, function: () -> T): T {
        return function.invoke()
    }

    @CacheEvict(value = [CACHE_NAME], key = "#key")
    fun <T> evict(key: String, function: () -> T): T {
        return function.invoke()
    }
}