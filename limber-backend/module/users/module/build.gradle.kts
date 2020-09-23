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

  implementation(project(":limber-backend:module:orgs:interface"))
  api(project(":limber-backend:module:users:interface"))

  testImplementation(project(":limber-backend:deprecated:common:sql:testing"))
  testImplementation(project(":limber-backend:deprecated:common:testing"))
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
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
