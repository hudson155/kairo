import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  implementation(project(":limber-backend-application:common:module"))
  implementation(project(":limber-backend-application:common:sql"))
  implementation(project(":limber-backend-application:module:orgs:orgs-rest-interface"))
  api(project(":limber-backend-application:module:orgs:orgs-service-interface"))
  implementation(project(":piper:serialization"))
  testImplementation(project(":limber-backend-application:common:sql:testing"))
  testImplementation(project(":limber-backend-application:common:testing"))
}

tasks.withType<KotlinCompile<*>>().configureEach {
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
