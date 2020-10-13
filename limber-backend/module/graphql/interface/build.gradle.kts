plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(project(":limber-backend:common:exceptions"))
  api(project(":limber-backend:common:rest-interface"))
  implementation(project(":limber-backend:common:util"))

  api(project(":limber-backend:deprecated:common"))
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
