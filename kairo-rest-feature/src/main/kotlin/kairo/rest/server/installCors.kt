package kairo.rest.server

import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS
import kairo.rest.KairoCorsConfig

internal fun Application.installCors(config: KairoCorsConfig) {
  install(CORS) {
    config.hosts.forEach { (host, scheme) ->
      allowHost(host = host, schemes = listOf(scheme))
    }
    config.headers.forEach { header ->
      allowHeader(header)
    }
    HttpMethod.DefaultMethods.forEach { method ->
      allowMethod(method)
    }
    allowCredentials = false
    allowSameOrigin = false
  }
}
