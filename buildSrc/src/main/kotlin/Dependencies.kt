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

  object Ktor {
    const val httpJvm: String = "io.ktor:ktor-http-jvm:${Versions.ktor}"
  }

  object Logging {
    const val slf4j: String = "org.slf4j:slf4j-api:${Versions.slf4j}"
  }
}
