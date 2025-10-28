plugins {
  id("kairo-platform")
  id("kairo-platform-publish")
}

dependencies {
  api(platform(project(":bom")))

  // arrow
  // https://github.com/arrow-kt/arrow/releases
  api(platform("io.arrow-kt:arrow-stack:2.1.2"))

  // coroutines
  // https://github.com/Kotlin/kotlinx.coroutines/releases
  api(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.10.2"))

  // exposed
  // https://github.com/JetBrains/Exposed/releases
  api(platform("org.jetbrains.exposed:exposed-bom:1.0.0-rc-1"))

  // gcp
  // https://github.com/googleapis/java-cloud-bom/releases
  api(platform("com.google.cloud:libraries-bom:26.68.0"))

  // guava
  // https://github.com/google/guava/releases
  api(platform("com.google.guava:guava-bom:33.4.8-jre"))

  // hikari
  // https://github.com/brettwooldridge/HikariCP/tags
  val hikariVersion = "7.0.2"
  constraints.api("com.zaxxer:HikariCP:$hikariVersion")

  // hocon
  // https://github.com/lightbend/config/releases
  val hoconVersion = "1.4.5"
  constraints.api("com.typesafe:config:$hoconVersion")

  // koin
  // https://github.com/InsertKoinIO/koin/releases
  api(platform("io.insert-koin:koin-bom:4.1.1"))

  // koin-annotations
  // https://github.com/InsertKoinIO/koin-annotations/releases
  api(platform("io.insert-koin:koin-annotations-bom:2.2.0"))

  // kotest
  // https://github.com/kotest/kotest/releases
  val kotestVersion = "6.0.3"
  constraints.api("io.kotest:kotest-runner-junit5:$kotestVersion")

  // kotlinx-datetime
  // https://github.com/Kotlin/kotlinx-datetime/releases
  val kotlinxDatetimeVersion = "0.7.1"
  constraints.api("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetimeVersion")

  // ktor
  // https://github.com/ktorio/ktor/releases
  api(platform("io.ktor:ktor-bom:3.3.0"))

  // log4j
  // https://github.com/apache/logging-log4j2/releases
  api(platform("org.apache.logging.log4j:log4j-bom:2.25.1"))

  // logging
  // https://github.com/oshai/kotlin-logging/releases
  val loggingVersion = "7.0.13"
  constraints.api("io.github.oshai:kotlin-logging-jvm:$loggingVersion")

  // mockk
  // https://github.com/mockk/mockk/releases
  val mockkVersion = "1.14.5"
  constraints.api("io.mockk:mockk:$mockkVersion")

  // postgres-gcp
  // https://github.com/GoogleCloudPlatform/cloud-sql-jdbc-socket-factory/releases
  val postgresGcpVersion = "1.25.3"
  constraints.api("com.google.cloud.sql:postgres-socket-factory:$postgresGcpVersion")

  // postgres-jdbc
  // https://github.com/pgjdbc/pgjdbc/releases
  val postgresJdbcVersion = "42.7.7"
  constraints.api("org.postgresql:postgresql:$postgresJdbcVersion")

  // serialization
  // https://github.com/Kotlin/kotlinx.serialization/releases
  api(platform("org.jetbrains.kotlinx:kotlinx-serialization-bom:1.9.0"))

  // slack
  // https://github.com/slackapi/java-slack-sdk/releases
  val slackVersion = "1.45.3"
  constraints.api("com.slack.api:slack-api-client:$slackVersion")
  constraints.api("com.slack.api:slack-api-client-kotlin-extension:$slackVersion")

  // slf4j
  // https://github.com/qos-ch/slf4j/releases
  api(platform("org.slf4j:slf4j-bom:2.0.17"))

  // testcontainers
  // https://github.com/testcontainers/testcontainers-java/releases
  api(platform("org.testcontainers:testcontainers-bom:1.21.3"))
}
