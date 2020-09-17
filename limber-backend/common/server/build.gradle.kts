plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(project(":limber-backend:common:config")) // Provides config to implementation projects
  implementation(project(":limber-backend:common:data-conversion"))
  implementation(project(":limber-backend:common:errors"))
  implementation(project(":limber-backend:common:exception-mapping"))
  implementation(project(":limber-backend:common:ktor-auth"))
  implementation(project(":limber-backend:common:module")) // For registering modules
  api(project(":limber-backend:common:serialization")) // Provides Json to implementation projects
  api(project(":limber-backend:common:util"))
  implementation(Dependencies.Jackson.moduleKotlin)
  implementation(Dependencies.Jackson.dataFormatYaml)
  implementation(Dependencies.Guice.guice)
  implementation(Dependencies.Ktor.serverHostCommon)
  api(Dependencies.Ktor.serverCore) // Implementation projects use this
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
