import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  implementation(project(":limber-backend:common:reps"))
  implementation(project(":limber-backend:common:serialization"))

  implementation(project(":limber-backend:deprecated:common:module"))
  implementation(project(":limber-backend:deprecated:common:sql"))

  api(project(":limber-backend:module:forms:service-interface"))
  implementation(project(":limber-backend:module:orgs:service-interface"))
  implementation(project(":limber-backend:module:users:service-interface"))

  implementation(Dependencies.Apache.csv)
  implementation(Dependencies.Jackson.annotations)

  testImplementation(project(":limber-backend:common:server"))
  testImplementation(project(":limber-backend:common:testing"))

  testImplementation(project(":limber-backend:deprecated:common:sql:testing"))

  testImplementation(project(":limber-backend:server:monolith"))
}

tasks.withType<KotlinCompile<*>>().configureEach {
  kotlinOptions.freeCompilerArgs += "-Xopt-in=io.limberapp.backend.LimberModule.Forms"
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
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
