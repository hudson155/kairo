import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin

plugins {
  kotlin("jvm")
}

allprojects {
  apply<KotlinPlatformJvmPlugin>()

  dependencies {
    implementation(kotlin("reflect"))
    implementation(project(":limber-multiplatform:logging"))
    testImplementation(kotlin("test-junit5"))
    testRuntimeOnly(Dependencies.JUnit.engine)
  }
}
