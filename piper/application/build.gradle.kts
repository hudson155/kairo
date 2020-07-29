plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  api(project(":piper:config")) // Provides config to implementation projects
  implementation(project(":piper:data-conversion"))
  implementation(project(":piper:errors"))
  implementation(project(":piper:exception-mapping"))
  implementation(project(":piper:ktor-auth"))
  implementation(project(":piper:module")) // For registering modules
  api(project(":piper:serialization")) // Provides Json to implementation projects
  api(project(":piper:types"))
  api(project(":piper:util"))
  implementation(Dependencies.Jackson.moduleKotlin) // For config loader
  implementation(Dependencies.Guice.guice)
  implementation(Dependencies.Ktor.serverHostCommon)
  api(Dependencies.Ktor.serverCore) // Implementation projects use this
}

detekt {
  config = files("$rootDir/.detekt/config.yml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
