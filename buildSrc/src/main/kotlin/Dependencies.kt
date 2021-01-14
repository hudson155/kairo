object Dependencies {
  object JUnit {
    const val engine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit}"
  }

  object Jackson {
    const val databind = "com.fasterxml.jackson.core:jackson-databind:${Versions.jackson}"
    const val moduleKotlin = "com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.jackson}"
  }

  object Logging {
    const val slf4j = "org.slf4j:slf4j-api:${Versions.slf4j}"
  }
}
