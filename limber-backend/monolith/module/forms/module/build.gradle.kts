import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
  id(Plugins.detekt)
}

dependencies {
  implementation(project(":limber-backend:common:reps"))
  implementation(project(":limber-backend:common:serialization"))
  implementation(project(":limber-backend:monolith:common:module"))
  implementation(project(":limber-backend:monolith:common:sql"))
  api(project(":limber-backend:monolith:module:forms:interface"))
  implementation(project(":limber-backend:monolith:module:orgs:interface"))
  implementation(project(":limber-backend:monolith:module:users:interface"))
  implementation(Dependencies.Apache.csv)
  testImplementation(project(":limber-backend:monolith:common:sql:testing"))
  testImplementation(project(":limber-backend:monolith:common:testing"))
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
