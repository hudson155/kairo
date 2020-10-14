plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  implementation(project(":limber-backend:common:reps"))
  implementation(project(":limber-backend:common:serialization"))

  implementation(project(":limber-backend:deprecated:common:module"))
  implementation(project(":limber-backend:deprecated:common:sql"))

  api(project(":limber-backend:module:auth:interface"))
  api(project(":limber-backend:module:auth:service-interface"))
  implementation(project(":limber-backend:module:orgs:service-interface"))
  implementation(project(":limber-backend:module:users:service-interface"))

  implementation(Dependencies.Bcrypt.jbcrypt)

  testImplementation(project(":limber-backend:common:testing:integration"))

  testImplementation(project(":limber-backend:deprecated:common:sql:testing"))

  testImplementation(project(":limber-backend:module:auth:client"))

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
