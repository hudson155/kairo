package io.limberapp.common.restInterface

import io.ktor.http.ContentType as KtorContentType

fun ContentType.forKtor() = when (this) {
  ContentType.CSV -> KtorContentType.Text.CSV
  ContentType.JSON -> KtorContentType.Application.Json
}
