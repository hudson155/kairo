plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(project(":limber-backend:common:finder"))
  implementation(project(":limber-backend:common:module"))
  implementation(project(":limber-backend:common:reps"))
  implementation(project(":limber-backend:common:serialization"))
  implementation(project(":limber-backend:common:sql"))

  api(project(":limber-backend:module:forms:interface"))
  implementation(project(":limber-backend:module:orgs:client"))
  implementation(project(":limber-backend:module:users:client"))

  implementation(Dependencies.Apache.csv)
  implementation(Dependencies.Jackson.annotations)

  testImplementation(project(":limber-backend:common:sql:testing"))
  testImplementation(project(":limber-backend:common:testing:integration"))

  testImplementation(project(":limber-backend:module:forms:client"))

  testImplementation(project(":limber-backend:server:monolith"))
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
