package kairo.rest.cors

import io.ktor.http.HttpMethod
import io.ktor.server.plugins.cors.CORSConfig

public fun CORSConfig.kairoConfigure(config: KairoCorsConfig) {
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
