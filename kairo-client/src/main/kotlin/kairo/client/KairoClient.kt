@file:Suppress("ForbiddenImport")

package kairo.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.CIOEngineConfig

public typealias KairoClient = HttpClient

public fun createKairoClient(
  block: HttpClientConfig<CIOEngineConfig>.() -> Unit = {},
): KairoClient =
  HttpClient(CIO, block)
