plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  implementation(project(":limber-backend:common:errors"))
  implementation(project(":limber-backend:common:exceptions"))
  implementation(project(":limber-backend:common:util"))
  implementation(Dependencies.Ktor.httpJvm)
}

detekt {
  config = files("$rootDir/.detekt/config.yml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
