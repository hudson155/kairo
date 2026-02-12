plugins {
  id("kairo-library")
  application
}

application {
  mainClass.set("kairo.adminSample.MainKt")
}

tasks.withType<Tar> { duplicatesStrategy = DuplicatesStrategy.EXCLUDE }
tasks.withType<Zip> { duplicatesStrategy = DuplicatesStrategy.EXCLUDE }

dependencies {
  implementation(project(":kairo-admin"))
  implementation(project(":kairo-application"))
  implementation(project(":kairo-config"))
  implementation(project(":kairo-dependency-injection:feature"))
  implementation(project(":kairo-exception"))
  implementation(project(":kairo-health-check:feature"))
  implementation(project(":kairo-hocon"))
  implementation(project(":kairo-id"))
  implementation(project(":kairo-optional"))
  implementation(project(":kairo-rest:endpoint"))
  implementation(project(":kairo-rest:feature"))
  implementation(project(":kairo-serialization"))
  implementation(project(":kairo-server"))
  implementation(project(":kairo-sql:feature"))

  implementation(libs.exposed)
  implementation(libs.exposed.r2dbc)
  implementation(libs.exposed.kotlinDatetime)
  implementation(libs.hocon)
  implementation(libs.koin)
  implementation(libs.koin.annotations)

  runtimeOnly(libs.postgres.r2dbc)
  runtimeOnly("org.slf4j:slf4j-simple")
}
