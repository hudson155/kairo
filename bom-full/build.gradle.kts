plugins {
  id("kairo-platform")
  id("kairo-platform-publish")
}

dependencies {
  api(platform(project(":bom")))

  // arrow
  api(platform(libs.arrow.bom))

  // coroutines
  api(platform(libs.coroutines.bom))

  // exposed
  api(platform(libs.exposed.bom))

  // gcp
  api(platform(libs.gcp.bom))

  // gcp-socket-factory
  constraints.api(libs.gcpSocketFactory.postgres)
  constraints.api(libs.gcpSocketFactory.r2dbcPostgres)

  // guava
  api(platform(libs.guava.bom))

  // jackson
  api(platform(libs.jackson.bom))

  // hocon
  constraints.api(libs.hocon)

  // koin
  api(platform(libs.koin.bom))

  // koin-annotations
  api(platform(libs.koinAnnotations.bom))

  // kotest
  constraints.api(libs.kotest)

  // kotlinx-datetime
  constraints.api(libs.datetime)

  // ktor
  api(platform(libs.ktor.bom))

  // log4j
  api(platform(libs.log4j.bom))

  // logging
  constraints.api(libs.logging)

  // mailersend
  constraints.api(libs.mailersend)

  // mockk
  constraints.api(libs.mockk)

  // moneta
  constraints.api(libs.moneta)

  // postgres-jdbc
  constraints.api(libs.postgres.jdbc)

  // postgres-r2dbc
  constraints.api(libs.postgres.r2dbc)

  // r2dbc-pool
  constraints.api(libs.r2dbc.pool)

  // slack
  constraints.api(libs.slack)
  constraints.api(libs.slack.kotlin)

  // slf4j
  api(platform(libs.slf4j.bom))

  // stytch
  constraints.api(libs.stytch)

  // testcontainers
  api(platform(libs.testcontainers.bom))
}
