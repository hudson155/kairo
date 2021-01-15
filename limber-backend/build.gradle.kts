import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin

plugins {
  kotlin("jvm")
}

allprojects {
  apply<KotlinPlatformJvmPlugin>()

  dependencies {
    implementation(kotlin("reflect"))
    if (path != ":limber-backend:common:util") {
      implementation(project(":limber-backend:common:util"))
    }
    implementation(Dependencies.Logging.slf4j)
    testImplementation(kotlin("test-junit5"))
    testRuntimeOnly(Dependencies.JUnit.engine)
  }
}
