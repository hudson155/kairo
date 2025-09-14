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
  api(platform("com.google.cloud:libraries-bom:26.67.0"))

  // guava
  // https://github.com/google/guava/releases
  api(platform("com.google.guava:guava-bom:33.4.8-jre"))

  // koin
  // https://github.com/InsertKoinIO/koin/releases
  api(platform("io.insert-koin:koin-bom:4.1.1"))

  // koin-annotations
  // https://github.com/InsertKoinIO/koin-annotations/releases
  api(platform("io.insert-koin:koin-annotations-bom:2.2.0-RC1"))

  // ktor
  // https://github.com/ktorio/ktor/releases
  api(platform("io.ktor:ktor-bom:3.2.3"))

  // log4j
  // https://github.com/apache/logging-log4j2/releases
  api(platform("org.apache.logging.log4j:log4j-bom:2.25.1"))

  // postgres-jdbc
  // https://github.com/pgjdbc/pgjdbc/releases
  api(platform("org.postgresql:postgresql:42.7.7"))

  // serialization
  // https://github.com/Kotlin/kotlinx.serialization/releases
  api(platform("org.jetbrains.kotlinx:kotlinx-serialization-bom:1.9.0"))

  // slf4j
  // https://github.com/qos-ch/slf4j/releases
  api(platform("org.slf4j:slf4j-bom:2.0.17"))

  // testcontainers
  // https://github.com/testcontainers/testcontainers-java/releases
  api(platform("org.testcontainers:testcontainers-bom:1.21.3"))
}
