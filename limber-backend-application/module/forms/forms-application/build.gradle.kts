plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation(project(":limber-backend-application:common:module"))
  implementation(project(":limber-backend-application:common:sql"))
  implementation(project(":limber-backend-application:module:forms:forms-rest-interface"))
  api(project(":limber-backend-application:module:forms:forms-service-interface"))
  implementation(project(":piper:serialization"))
  testImplementation(project(":limber-backend-application:common:sql:testing"))
  testImplementation(project(":limber-backend-application:common:testing"))
}

tasks.test {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
}

detekt {
  config = files("$rootDir/.detekt/config.yml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
