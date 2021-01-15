object Dependencies {
  object Google {
    const val guice = "com.google.inject:guice:${Versions.guice}"
  }

  object JUnit {
    const val engine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit}"
  }

  object Jackson {
    const val annotations = "com.fasterxml.jackson.core:jackson-annotations:${Versions.jackson}"
    const val databind = "com.fasterxml.jackson.core:jackson-databind:${Versions.jackson}"
    const val datatypeJsr310 = "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${Versions.jackson}"
    const val moduleKotlin = "com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.jackson}"
  }

  object Ktor {
    const val httpJvm = "io.ktor:ktor-http-jvm:${Versions.ktor}"
  }

  object Logging {
    const val slf4j = "org.slf4j:slf4j-api:${Versions.slf4j}"
  }
}
