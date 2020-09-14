plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(project(":limber-backend:common:config")) // Provides config to modules
  api(project(":limber-backend:common:exceptions")) // Provides exceptions to modules
  api(project(":limber-backend:common:ktor-auth")) // All modules use auth
  implementation(project(":limber-backend:common:reps"))
  api(project(":limber-backend:common:rest-interface")) // Allows modules to configure endpoints
  api(project(":limber-backend:common:serialization")) // All modules configure serialization
  api(project(":limber-backend:common:util"))
  api(Dependencies.Guice.guice) // Modules are Guice modules
  api(Dependencies.Ktor.serverCore) // Every modules needs this
}

detekt {
  config = files("$rootDir/.detekt/config.yml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
