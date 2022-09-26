package limber.rest.ktorPlugins

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.dataconversion.DataConversion
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.plugins.forwardedheaders.ForwardedHeaders
import limber.serialization.ObjectMapperFactory
import limber.serialization.StringTrimModule

internal fun Application.installHttpPlugins(
  allowedHosts: List<String>,
  serverName: String,
) {
  /**
   * https://ktor.io/docs/compression.html.
   *
   * Compression risks introducing susceptibility to the BREACH vulnerability.
   * This is mitigated by the fact that CORS rules prevent requests from unknown hosts.
   *
   * In order to allow traffic from all hosts, however (a public API),
   * we should explore different solutions.
   * HTB is an accepted solution (see https://ieeexplore.ieee.org/document/9754554),
   * which I have filed an issue with JetBrains for.
   * https://youtrack.jetbrains.com/issue/KTOR-4818/Server-HTTP-compression-HTB-solution-for-BREACH.
   */
  install(Compression) {
    default()
  }

  /**
   * https://ktor.io/docs/serialization.html.
   *
   * We use Jackson for serialization. See the serialization library.
   */
  install(ContentNegotiation) {
    val objectMapper = ObjectMapperFactory.builder(ObjectMapperFactory.Format.JSON).apply {
      addModule(StringTrimModule)
    }.build()
    register(contentType = ContentType.Application.Json, converter = JacksonConverter(objectMapper))
  }

  install(DataConversion)

  /**
   * https://ktor.io/docs/cors.html.
   *
   * Allowing credentials is not required
   * since we use the Authorization header instead of cookies or session information.
   */
  install(CORS) {
    allowCredentials = false
    allowedHosts.forEach { host ->
      // DO NOT enable wildcard hosts without properly mitigating the BREACH vulnerability.
      // See the installation of the [Compression] feature for more information.
      require(host != "*")
      allowHost(host)
    }
    allowHeader(HttpHeaders.Authorization)
    allowHeader(HttpHeaders.ContentType)
    HttpMethod.DefaultMethods.forEach { allowMethod(it) }
  }

  /**
   * https://ktor.io/docs/default-headers.html.
   */
  install(DefaultHeaders) {
    header(HttpHeaders.Server, serverName)
  }

  /**
   * https://ktor.io/docs/forward-headers.html.
   */
  install(ForwardedHeaders)
}
