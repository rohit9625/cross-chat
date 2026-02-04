package dev.androhit.crosschat.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class CrossChatApi(val httpClient: HttpClient) {

    suspend inline fun <reified T> get(url: String): T {
        return httpClient.get(url).body()
    }

    suspend inline fun post(path: String, body: Any? = null): HttpResponse {
        return httpClient.post(path) {
            if (body != null) {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
        }
    }

    suspend inline fun put(path: String, body: Any? = null): HttpResponse {
        return httpClient.put(path) {
            if (body != null) {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
        }
    }

    suspend inline fun patch(path: String, body: Any? = null): HttpResponse {
        return httpClient.patch(path) {
            if (body != null) {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
        }
    }

    suspend inline fun delete(path: String): HttpResponse {
        return httpClient.delete(path)
    }
}