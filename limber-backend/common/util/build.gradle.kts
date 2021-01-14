import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

tasks.withType<KotlinJvmCompile>().configureEach {
  kotlinOptions.freeCompilerArgs += "-Xallow-kotlin-package"
}
