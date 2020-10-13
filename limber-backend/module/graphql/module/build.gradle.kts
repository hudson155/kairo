plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  implementation(project(":limber-backend:common:module"))
  implementation(project(":limber-backend:common:reps"))

  implementation(project(":limber-backend:deprecated:common:module"))

  api(project(":limber-backend:module:graphql:interface"))

  testImplementation(project(":limber-backend:common:testing:integration"))

  testImplementation(project(":limber-backend:module:graphql:client"))
}

tasks.test {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
