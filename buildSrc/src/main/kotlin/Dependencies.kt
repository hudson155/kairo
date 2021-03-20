object Dependencies {
  object Bcrypt {
    val jbcrypt: String = "org.mindrot:jbcrypt"
        .version(Versions.jbcrypt)
  }

  object Google {
    val guice: String = "com.google.inject:guice"
        .version(Versions.guice)
  }

  object JUnit {
    val api: String = "org.junit.jupiter:junit-jupiter-api"
        .version(Versions.junit)
    val engine: String = "org.junit.jupiter:junit-jupiter-engine"
        .version(Versions.junit)
  }

  object Jackson {
    val annotations: String = "com.fasterxml.jackson.core:jackson-annotations"
        .version(Versions.jackson)
    val databind: String = "com.fasterxml.jackson.core:jackson-databind"
        .version(Versions.jackson)
    val dataformatYaml: String = "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml"
        .version(Versions.jackson)
    val datatypeJsr310: String = "com.fasterxml.jackson.datatype:jackson-datatype-jsr310"
        .version(Versions.jackson)
    val moduleKotlin: String = "com.fasterxml.jackson.module:jackson-module-kotlin"
        .version(Versions.jackson)
  }

  object Jwt {
    val auth0JavaJwt: String = "com.auth0:java-jwt"
        .version(Versions.auth0JavaJwt)
    val auth0JwksRsa: String = "com.auth0:jwks-rsa"
        .version(Versions.auth0JwksRsa)
  }

  object Ktor {
    val auth: String = "io.ktor:ktor-auth"
        .version(Versions.ktor)
    val clientCio: String = "io.ktor:ktor-client-cio"
        .version(Versions.ktor)
    val clientJackson: String = "io.ktor:ktor-client-jackson"
        .version(Versions.ktor)
    val clientMock: String = "io.ktor:ktor-client-mock"
        .version(Versions.ktor)
    val httpJvm: String = "io.ktor:ktor-http-jvm"
        .version(Versions.ktor)
    val jackson: String = "io.ktor:ktor-jackson"
        .version(Versions.ktor)
    val serverCio: String = "io.ktor:ktor-server-cio"
        .version(Versions.ktor)
    val serverCore: String = "io.ktor:ktor-server-core"
        .version(Versions.ktor)
    val serverHostCommon: String = "io.ktor:ktor-server-host-common"
        .version(Versions.ktor)
    val serverTest: String = "io.ktor:ktor-server-test-host"
        .version(Versions.ktor)
  }

  object Logging {
    val logbackClassic: String = "ch.qos.logback:logback-classic"
        .version(Versions.logback)
    val slf4j: String = "org.slf4j:slf4j-api"
        .version(Versions.slf4j)
  }

  object MockK {
    val mockK: String = "io.mockk:mockk"
        .version(Versions.mockK)
  }

  object Sql {
    val flyway: String = "org.flywaydb:flyway-core"
        .version(Versions.flyway)
    val hikari: String = "com.zaxxer:HikariCP"
        .version(Versions.hikari)
    val jdbi3Kotlin: String = "org.jdbi:jdbi3-kotlin"
        .version(Versions.jdbi3)
    val jdbi3KotlinSqlobject: String = "org.jdbi:jdbi3-kotlin-sqlobject"
        .version(Versions.jdbi3)
    val jdbi3Postgres: String = "org.jdbi:jdbi3-postgres"
        .version(Versions.jdbi3)
    val postgres: String = "org.postgresql:postgresql"
        .version(Versions.postgres)
  }

  private fun String.version(version: String): String = "$this:$version"
}
