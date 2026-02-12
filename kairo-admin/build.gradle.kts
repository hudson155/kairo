plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  compileOnly(project(":kairo-feature"))
  compileOnly(project(":kairo-rest"))
  compileOnly(project(":kairo-rest:endpoint"))
  implementation(project(":kairo-logging"))
  implementation(project(":kairo-ktor"))

  implementation(libs.ktorServer)
  implementation(libs.ktorServer.auth)
  implementation(libs.kotlinxHtml)
  implementation(libs.ktorServer.htmlBuilder)

  compileOnly(libs.koin)
  compileOnly(libs.hocon)
  compileOnly(libs.r2dbc.pool)
  compileOnly(libs.postgres.r2dbc)
}
