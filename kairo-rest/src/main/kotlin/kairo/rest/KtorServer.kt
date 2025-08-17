package kairo.rest

import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.netty.NettyApplicationEngine

internal typealias KtorServer = EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration>
