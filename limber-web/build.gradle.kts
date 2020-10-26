import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
  kotlin("js")
  kotlin("plugin.serialization") version Versions.kotlin
  id(Plugins.detekt)
}

repositories {
  maven(url = "https://kotlin.bintray.com/kotlin-js-wrappers/")
}

group = "io.limberapp.web"
version = "0.1.0-SNAPSHOT"

dependencies {
  implementation("org.jetbrains:kotlin-extensions:1.0.1-pre.112-kotlin-${Versions.kotlin}")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.3.9")
  implementation("org.jetbrains:kotlin-react:16.13.1-pre.112-kotlin-${Versions.kotlin}")
  implementation("org.jetbrains:kotlin-react-dom:16.13.1-pre.112-kotlin-${Versions.kotlin}")
  implementation("org.jetbrains:kotlin-react-router-dom:5.1.2-pre.112-kotlin-${Versions.kotlin}")
  implementation("org.jetbrains:kotlin-styled:1.0.0-pre.112-kotlin-${Versions.kotlin}")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:1.0-M1-1.4.0-rc")
  implementation(npm("@auth0/auth0-spa-js", "1.6.5"))
  implementation(npm("jsonwebtoken", "8.5.1"))
}

kotlin.target.browser {}
kotlin.target.useCommonJs()

tasks.withType<KotlinCompile<*>>().configureEach {
  kotlinOptions.freeCompilerArgs += "-Xallow-kotlin-package"
}

detekt {
  config = files("$projectDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
