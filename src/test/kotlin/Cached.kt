import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.EnableCaching
import org.springframework.test.context.TestPropertySource
import cacheAble.Cacheable
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.cache.CacheManager
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@TestPropertySource(properties = ["spring.cache.type=simple"])
@ContextConfiguration(classes = [CacheTestConfig::class])
class CacheUserTest {

    @Autowired
    lateinit var cacheable: Cacheable

    @Test
    fun `cache should store and retrieve values correctly`() {
        val result1 = cacheable.cache("User", 123) { "Hello, User 123!" }

        assertEquals("Hello, User 123!", result1)

        val result2 = cacheable.cache("User", 123) { "New Data!" }

        assertEquals("Hello, User 123!", result2)
    }

    // caching 처리가 진행된다면, counter는 내부 함수를 타지 않을것이기 떄문에 1로 고정이 된다.
    @Test
    fun `cache should call function only once for the same key`() {
        var counter = 0

        val result1 = cacheable.cache("User", 123) {
            counter++
            "Hello, User 123!"
        }

        val result2 = cacheable.cache("User", 123) {
            counter++
            "New Data!"
        }

        assertEquals("Hello, User 123!", result1)
        assertEquals("Hello, User 123!", result2)
        assertEquals(1, counter, "Function should be called only once")
    }
}

@Configuration
@EnableCaching
@ComponentScan("cacheAble")
class CacheTestConfig {
    @Bean
    fun cacheManager(): CacheManager {
        return ConcurrentMapCacheManager("default:key") // 캐시 이름을 지정
    }
}