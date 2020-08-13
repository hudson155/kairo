object Dependencies {
  object Apache {
    const val csv = "org.apache.commons:commons-csv:${Versions.apacheCsv}"
  }

  object Bcrypt {
    const val jbcrypt = "org.mindrot:jbcrypt:${Versions.jbcrypt}"
  }

  object Guice {
    const val guice = "com.google.inject:guice:${Versions.guice}"
  }

  object JUnit {
    const val api = "org.junit.jupiter:junit-jupiter-api:${Versions.junit}"
    const val engine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit}"
  }

  object Jackson {
    const val dataFormatYaml = "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:${Versions.jackson}"
    const val moduleKotlin = "com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.jackson}"
  }

  object Jwt {
    const val auth0JavaJwt = "com.auth0:java-jwt:${Versions.auth0JavaJwt}"
    const val auth0JwksRsa = "com.auth0:jwks-rsa:${Versions.auth0JwksRsa}"
  }

  object Kotlin {
    const val extensions = "org.jetbrains:kotlin-extensions:${Versions.kotlinExtensions}-${Versions.kotlinJsWrappers}-kotlin-${Versions.kotlin}"
  }

  object KotlinJs {
    const val react = "org.jetbrains:kotlin-react:${Versions.react}-${Versions.kotlinJsWrappers}-kotlin-${Versions.kotlin}"
    const val reactDom = "org.jetbrains:kotlin-react-dom:${Versions.react}-${Versions.kotlinJsWrappers}-kotlin-${Versions.kotlin}"
    const val reactRouterDom = "org.jetbrains:kotlin-react-router-dom:${Versions.reactRouter}-${Versions.kotlinJsWrappers}-kotlin-${Versions.kotlin}"
    const val styledComponents = "org.jetbrains:kotlin-styled:${Versions.styledComponents}-${Versions.kotlinJsWrappers}-kotlin-${Versions.kotlin}"
  }

  object Kotlinx {
    const val coroutinesJs = "org.jetbrains.kotlinx:kotlinx-coroutines-core-js:${Versions.kotlinxCoroutines}"
  }

  object Ktor {
    const val auth = "io.ktor:ktor-auth:${Versions.ktor}"
    const val httpJvm = "io.ktor:ktor-http-jvm:${Versions.ktor}"
    const val serverCio = "io.ktor:ktor-server-cio:${Versions.ktor}"
    const val serverCore = "io.ktor:ktor-server-core:${Versions.ktor}"
    const val serverHostCommon = "io.ktor:ktor-server-host-common:${Versions.ktor}"
    const val test = "io.ktor:ktor-server-test-host:${Versions.ktor}"
  }

  object Logging {
    const val logbackClassic = "ch.qos.logback:logback-classic:${Versions.logback}"
    const val slf4j = "org.slf4j:slf4j-api:${Versions.slf4j}"
  }

  object MockK {
    const val mockK = "io.mockk:mockk:${Versions.mockK}"
  }

  object Serialization {
    const val common = "org.jetbrains.kotlinx:kotlinx-serialization-runtime:${Versions.serialization}"
    const val js = "org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:${Versions.serialization}"
    const val jvm = "org.jetbrains.kotlinx:kotlinx-serialization-runtime:${Versions.serialization}"
  }

  object Sql {
    const val flyway = "org.flywaydb:flyway-core:${Versions.flyway}"
    const val hikari = "com.zaxxer:HikariCP:${Versions.hikari}"
    const val jdbi3Kotlin = "org.jdbi:jdbi3-kotlin:${Versions.jdbi3}"
    const val jdbi3KotlinSqlobject = "org.jdbi:jdbi3-kotlin-sqlobject:${Versions.jdbi3}"
    const val jdbi3Postgres = "org.jdbi:jdbi3-postgres:${Versions.jdbi3}"
    const val postgres = "org.postgresql:postgresql:${Versions.postgres}"
  }
}
