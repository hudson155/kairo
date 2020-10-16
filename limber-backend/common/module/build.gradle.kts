plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(project(":limber-backend:common:config"))
  api(project(":limber-backend:common:exceptions"))
  api(project(":limber-backend:common:ktor-auth"))
  implementation(project(":limber-backend:common:reps"))
  api(project(":limber-backend:common:rest-interface"))
  api(project(":limber-backend:common:serialization"))
  api(project(":limber-backend:common:util"))

  api(Dependencies.Google.guice)
  api(Dependencies.Ktor.serverCore)
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
