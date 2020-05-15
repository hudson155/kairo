plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation(project(":limber-backend-application:common"))
}

detekt {
  config = files("$rootDir/.detekt/config.yml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
