package com.piperframework.restInterface

import io.ktor.http.HttpMethod as KtorHttpMethod

fun HttpMethod.forKtor() = when (this) {
    HttpMethod.DELETE -> KtorHttpMethod.Delete
    HttpMethod.GET -> KtorHttpMethod.Get
    HttpMethod.PATCH -> KtorHttpMethod.Patch
    HttpMethod.POST -> KtorHttpMethod.Post
    HttpMethod.PUT -> KtorHttpMethod.Put
}
