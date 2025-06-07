@file:Suppress("ForbiddenImport")

package kairo.rest.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.java.Java
import io.ktor.client.engine.java.JavaHttpConfig

public typealias KairoClient = HttpClient

public fun createKairoClient(
  block: HttpClientConfig<JavaHttpConfig>.() -> Unit = {},
): KairoClient =
  HttpClient(Java) {
    expectSuccess = true
    block()
  }
