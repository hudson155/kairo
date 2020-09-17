import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
  kotlin("js")
  kotlin("plugin.serialization") version Versions.kotlin
  id(Plugins.detekt)
}

group = "io.limberapp.web"
version = "0.1.0-SNAPSHOT"

dependencies {
  implementation(Dependencies.Kotlin.extensions)
  implementation(Dependencies.Kotlinx.coroutinesJs)
  implementation(Dependencies.KotlinJs.react)
  implementation(Dependencies.KotlinJs.reactDom)
  implementation(Dependencies.KotlinJs.reactRouterDom)
  implementation(Dependencies.KotlinJs.styledComponents)
  implementation(Dependencies.Serialization.js)
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
