package limber.gradle

object Dependencies {
  object Auth {
    val auth0JavaJwt: String = "com.auth0:java-jwt"
      .version(Versions.auth0JavaJwt)
    val auth0JwksRsa: String = "com.auth0:jwks-rsa"
      .version(Versions.auth0JwksRsa)
  }

  object Detekt {
    val formatting: String = "io.gitlab.arturbosch.detekt:detekt-formatting"
      .version(Versions.detekt)
  }

  object Gcp {
    val secretManager: String = "com.google.cloud:google-cloud-secretmanager"
      .version(Versions.gcpSecretManager)
  }

  object Google {
    val guice: String = "com.google.inject:guice"
      .version(Versions.guice)
  }

  object Jackson {
    val databind: String = "com.fasterxml.jackson.core:jackson-databind"
      .version(Versions.jackson)

    object DataFormat {
      val yaml: String = "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml"
        .version(Versions.jackson)
    }

    object DataType {
      val jsr310: String = "com.fasterxml.jackson.datatype:jackson-datatype-jsr310"
        .version(Versions.jackson)
    }

    object Module {
      val kotlin: String = "com.fasterxml.jackson.module:jackson-module-kotlin"
        .version(Versions.jackson)
    }
  }

  object JavaExpressionLanguage {
    val expressly: String = "org.glassfish.expressly:expressly"
      .version(Versions.expressly)
  }

  object Kotlinx {
    val coroutines: String = "org.jetbrains.kotlinx:kotlinx-coroutines-core"
      .version(Versions.kotlinxCoroutines)
    val slf4j: String = "org.jetbrains.kotlinx:kotlinx-coroutines-slf4j"
      .version(Versions.kotlinxCoroutines)
  }

  object Ktor {
    val httpJvm: String = "io.ktor:ktor-http-jvm"
      .version(Versions.ktor)

    object Client {
      val cio: String = "io.ktor:ktor-client-cio"
        .version(Versions.ktor)
      val contentNegotiation: String = "io.ktor:ktor-client-content-negotiation"
        .version(Versions.ktor)
      val coreJvm: String = "io.ktor:ktor-client-core-jvm"
        .version(Versions.ktor)
    }

    object Serialization {
      val jackson: String = "io.ktor:ktor-serialization-jackson"
        .version(Versions.ktor)
    }

    object Server {
      val authJwt: String = "io.ktor:ktor-server-auth-jwt"
        .version(Versions.ktor)
      val authJvm: String = "io.ktor:ktor-server-auth-jvm"
        .version(Versions.ktor)
      val autoHeadResponse: String = "io.ktor:ktor-server-auto-head-response"
        .version(Versions.ktor)
      val cio: String = "io.ktor:ktor-server-cio"
        .version(Versions.ktor)
      val compression: String = "io.ktor:ktor-server-compression"
        .version(Versions.ktor)
      val contentNegotiation: String = "io.ktor:ktor-server-content-negotiation"
        .version(Versions.ktor)
      val coreJvm: String = "io.ktor:ktor-server-core-jvm"
        .version(Versions.ktor)
      val cors: String = "io.ktor:ktor-server-cors"
        .version(Versions.ktor)
      val dataConversion: String = "io.ktor:ktor-server-data-conversion"
        .version(Versions.ktor)
      val defaultHeaders: String = "io.ktor:ktor-server-default-headers"
        .version(Versions.ktor)
      val doubleReceive: String = "io.ktor:ktor-server-double-receive"
        .version(Versions.ktor)
      val forwardedHeaders: String = "io.ktor:ktor-server-forwarded-header"
        .version(Versions.ktor)
      val hostCommonJvm: String = "io.ktor:ktor-server-host-common-jvm"
        .version(Versions.ktor)
      val statusPages: String = "io.ktor:ktor-server-status-pages"
        .version(Versions.ktor)
    }
  }

  object Logging {
    val kotlinLogging: String = "io.github.microutils:kotlin-logging-jvm"
      .version(Versions.kotlinLogging)

    object Log4j {
      val core: String = "org.apache.logging.log4j:log4j-core"
        .version(Versions.log4j)
      val layoutTemplateJson: String = "org.apache.logging.log4j:log4j-layout-template-json"
        .version(Versions.log4j)
      val slf4jImpl: String = "org.apache.logging.log4j:log4j-slf4j-impl"
        .version(Versions.log4j)
    }
  }

  object Sql {
    val flyway: String = "org.flywaydb:flyway-core"
      .version(Versions.flyway)

    val hikari: String = "com.zaxxer:HikariCP"
      .version(Versions.hikari)

    object Jdbi3 {
      val kotlin: String = "org.jdbi:jdbi3-kotlin"
        .version(Versions.jdbi3)
      val postgres: String = "org.jdbi:jdbi3-postgres"
        .version(Versions.jdbi3)
    }

    val postgres: String = "org.postgresql:postgresql"
      .version(Versions.postgres)
  }

  object Testing {
    object Junit {
      val api: String = "org.junit.jupiter:junit-jupiter-api"
        .version(Versions.junit)
      val engine: String = "org.junit.jupiter:junit-jupiter-engine"
        .version(Versions.junit)
      val params: String = "org.junit.jupiter:junit-jupiter-params"
        .version(Versions.junit)
    }

    object Kotest {
      val assertions: String = "io.kotest:kotest-assertions-core"
        .version(Versions.kotest)
    }

    val mockK: String = "io.mockk:mockk"
      .version(Versions.mockK)
  }

  object Validation {
    val hibernate: String = "org.hibernate.validator:hibernate-validator"
      .version(Versions.hibernateValidator)
  }

  private fun String.version(version: String): String = "$this:$version"
}
