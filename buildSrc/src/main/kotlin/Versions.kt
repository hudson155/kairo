object Versions {
  // https://github.com/auth0/java-jwt/releases
  const val auth0JavaJwt: String = "3.13.0"

  // https://github.com/auth0/jwks-rsa-java/releases
  // TODO: 0.16.0 is released but doesn't seem to be available.
  const val auth0JwksRsa: String = "0.15.0"

  // https://github.com/detekt/detekt/releases
  const val detekt: String = "1.15.0"

  // https://github.com/flyway/flyway/releases
  const val flyway: String = "7.5.4"

  // https://github.com/google/guice/releases
  const val guice: String = "4.2.3"

  // https://github.com/brettwooldridge/HikariCP/releases
  const val hikari: String = "4.0.2"

  // https://github.com/FasterXML/jackson-core/releases
  // TODO: There seems to be a bug in newer versions of Jackson that causes deserialization of Unit
  //  to not use the singleton instance.
  //  https://github.com/FasterXML/jackson-module-kotlin/issues/196#issuecomment-760759070.
  const val jackson: String = "2.10.5"

  // https://github.com/jeremyh/jBCrypt/releases
  const val jbcrypt: String = "0.4"

  // https://github.com/jdbi/jdbi/releases
  const val jdbi3: String = "3.18.0"

  // https://github.com/junit-team/junit5/releases
  const val junit: String = "5.7.1"

  // https://github.com/JetBrains/kotlin/releases
  const val kotlin: String = "1.4.31"

  // https://github.com/ktorio/ktor/releases
  const val ktor: String = "1.5.2"

  // https://github.com/qos-ch/logback/releases
  const val logback: String = "1.2.3"

  // https://github.com/mockk/mockk/releases
  const val mockK: String = "1.10.6"

  // https://github.com/pgjdbc/pgjdbc/releases
  const val postgres: String = "42.2.19"

  // https://github.com/johnrengelman/shadow/releases
  const val shadow: String = "6.1.0"

  // https://github.com/qos-ch/slf4j/releases
  const val slf4j: String = "1.7.30"
}
