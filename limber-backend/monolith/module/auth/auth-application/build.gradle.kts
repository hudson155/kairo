import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  implementation(project(":limber-backend:monolith:common:module"))
  implementation(project(":limber-backend:monolith:common:sql"))
  implementation(project(":limber-backend:monolith:module:auth:auth-rest-interface"))
  api(project(":limber-backend:monolith:module:auth:auth-service-interface"))
  implementation(project(":limber-backend:monolith:module:orgs:orgs-service-interface"))
  implementation(project(":limber-backend:monolith:module:users:users-service-interface"))
  implementation(project(":piper:serialization"))
  implementation(Dependencies.Bcrypt.jbcrypt)
  testImplementation(project(":limber-backend:monolith:common:sql:testing"))
  testImplementation(project(":limber-backend:monolith:common:testing"))
}

tasks.withType<KotlinCompile<*>>().configureEach {
  kotlinOptions.freeCompilerArgs += "-Xopt-in=io.limberapp.backend.LimberModule.Auth"
}
tasks.compileTestKotlin {
  kotlinOptions.freeCompilerArgs += "-Xopt-in=io.limberapp.backend.LimberModule.Orgs"
  kotlinOptions.freeCompilerArgs += "-Xopt-in=io.limberapp.backend.LimberModule.Users"
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
