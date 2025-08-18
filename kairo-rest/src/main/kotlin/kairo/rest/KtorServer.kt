package kairo.rest

import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.netty.NettyApplicationEngine

public typealias KtorServerEngine = NettyApplicationEngine

public typealias KtorServerConfig = NettyApplicationEngine.Configuration

public typealias KtorServer = EmbeddedServer<KtorServerEngine, KtorServerConfig>
