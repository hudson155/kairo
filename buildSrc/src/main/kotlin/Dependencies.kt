@Suppress("Reformat") // One line per dependency is preferred here.
object Dependencies {
  object Google {
    const val guice: String = "com.google.inject:guice:${Versions.guice}"
  }

  object JUnit {
    const val engine: String = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit}"
  }

  object Jackson {
    const val annotations: String = "com.fasterxml.jackson.core:jackson-annotations:${Versions.jackson}"
    const val databind: String = "com.fasterxml.jackson.core:jackson-databind:${Versions.jackson}"
    const val dataformatYaml: String = "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:${Versions.jackson}"
    const val datatypeJsr310: String = "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${Versions.jackson}"
    const val moduleKotlin: String = "com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.jackson}"
  }

  object Jwt {
    const val auth0JavaJwt: String = "com.auth0:java-jwt:${Versions.auth0JavaJwt}"
    const val auth0JwksRsa: String = "com.auth0:jwks-rsa:${Versions.auth0JwksRsa}"
  }

  object Ktor {
    const val auth: String = "io.ktor:ktor-auth:${Versions.ktor}"
    const val clientCio: String = "io.ktor:ktor-client-cio:${Versions.ktor}"
    const val clientJackson: String = "io.ktor:ktor-client-jackson:${Versions.ktor}"
    const val clientMock: String = "io.ktor:ktor-client-mock:${Versions.ktor}"
    const val httpJvm: String = "io.ktor:ktor-http-jvm:${Versions.ktor}"
    const val serverCore: String = "io.ktor:ktor-server-core:${Versions.ktor}"
    const val serverTest: String = "io.ktor:ktor-server-test-host:${Versions.ktor}"
  }

  object Logging {
    const val logbackClassic: String = "ch.qos.logback:logback-classic:${Versions.logback}"
    const val slf4j: String = "org.slf4j:slf4j-api:${Versions.slf4j}"
  }
}
