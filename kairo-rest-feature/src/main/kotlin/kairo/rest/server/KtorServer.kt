package kairo.rest.server

import io.ktor.server.cio.CIOApplicationEngine
import io.ktor.server.engine.EmbeddedServer

internal typealias KtorServer = EmbeddedServer<CIOApplicationEngine, CIOApplicationEngine.Configuration>
