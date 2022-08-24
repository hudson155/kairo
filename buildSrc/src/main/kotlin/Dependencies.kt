object Dependencies {
  object Detekt {
    val formatting: String = "io.gitlab.arturbosch.detekt:detekt-formatting"
      .version(Versions.detekt)
  }

  object Gcp {
    val secretManager: String = "com.google.cloud:google-cloud-secretmanager"
      .version(Versions.gcpSecretManager)
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
  }

  private fun String.version(version: String): String =
    "$this:$version"
}
