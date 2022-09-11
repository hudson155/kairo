object Dependencies {
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

  object Kotlinx {
    val coroutines: String = "org.jetbrains.kotlinx:kotlinx-coroutines-core"
      .version(Versions.kotlinx)
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

  object Testing {
    object Junit {
      val api: String = "org.junit.jupiter:junit-jupiter-api"
        .version(Versions.junit)
      val engine: String = "org.junit.jupiter:junit-jupiter-engine"
        .version(Versions.junit)
    }

    object Kotest {
      val assertions: String = "io.kotest:kotest-assertions-core"
        .version(Versions.kotest)
    }

    val mockK: String = "io.mockk:mockk"
      .version(Versions.mockK)
  }

  private fun String.version(version: String): String =
    "$this:$version"
}
