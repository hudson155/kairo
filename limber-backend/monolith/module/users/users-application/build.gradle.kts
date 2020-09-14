import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  implementation(project(":limber-backend:monolith:common:module"))
  implementation(project(":limber-backend:monolith:common:sql"))
  implementation(project(":limber-backend:monolith:module:users:users-rest-interface"))
  api(project(":limber-backend:monolith:module:users:users-service-interface"))
  implementation(project(":limber-backend:monolith:module:orgs:orgs-service-interface"))
  implementation(project(":piper:serialization"))
  testImplementation(project(":limber-backend:monolith:common:sql:testing"))
  testImplementation(project(":limber-backend:monolith:common:testing"))
}

tasks.withType<KotlinCompile<*>>().configureEach {
  kotlinOptions.freeCompilerArgs += "-Xopt-in=io.limberapp.backend.LimberModule.Users"
}
tasks.compileTestKotlin {
  kotlinOptions.freeCompilerArgs += "-Xopt-in=io.limberapp.backend.LimberModule.Orgs"
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
