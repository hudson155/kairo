object Dependencies {
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
