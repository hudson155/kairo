plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  compileOnly(project(":kairo-feature"))
  compileOnly(project(":kairo-rest"))
  compileOnly(project(":kairo-rest:endpoint"))
  compileOnly(project(":kairo-dependency-injection:feature"))
  compileOnly(project(":kairo-health-check:feature"))
  compileOnly(project(":kairo-logging"))
  compileOnly(project(":kairo-ktor"))

  implementation(libs.ktorServer)
  implementation(libs.ktorServer.auth)
  implementation(libs.kotlinxHtml)
  implementation(libs.ktorServer.htmlBuilder)

  compileOnly(project(":kairo-slack"))
  compileOnly(project(":kairo-slack:feature"))
  compileOnly(project(":kairo-stytch"))
  compileOnly(project(":kairo-stytch:feature"))
  compileOnly(project(":kairo-mailersend"))
  compileOnly(project(":kairo-mailersend:feature"))

  compileOnly(libs.koin)
  compileOnly(libs.hocon)
  compileOnly(libs.r2dbc.pool)
  compileOnly(libs.postgres.r2dbc)
}
