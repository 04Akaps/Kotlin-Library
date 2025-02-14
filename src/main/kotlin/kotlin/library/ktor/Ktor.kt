package kotlin.library.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Configuration
class HttpClientConfig {

    @Bean
    fun httpClient() : HttpClient {
        return HttpClient(CIO) {
            engine {
                requestTimeout = 30_000 // 30초 (밀리초 단위)
            }

            install(DefaultRequest) {
                headers.appendIfNameAbsent(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            }
        }
    }
}


@Component
class KtorClient(private val http: HttpClient) {

    suspend fun GET(url: String, headers: Map<String, String> = emptyMap()): HttpResponse {
        return request(HttpMethod.Get, url, headers)
    }

    suspend fun POST(url: String, body: Any, headers: Map<String, String> = emptyMap()): HttpResponse {
        return request(HttpMethod.Post, url, headers, body)
    }

    suspend fun PUT(url: String, body: Any, headers: Map<String, String> = emptyMap()): HttpResponse {
        return request(HttpMethod.Put, url, headers, body)
    }

    suspend fun DELETE(url: String, headers: Map<String, String> = emptyMap()): HttpResponse {
        return request(HttpMethod.Delete, url, headers)
    }

    private suspend fun  request(
        method: HttpMethod,
        url: String,
        headers: Map<String, String>,
        body: Any? = null
    ): HttpResponse {
        return http.request(url) {
            this.method = method
            headers.forEach { (key, value) -> header(key, value) }
            contentType(ContentType.Application.Json)
            body?.let { setBody(it) }
        }
    }
}