plugins {
  kotlin("js")
  id(Plugins.detekt)
}

group = "io.limberapp.web"
version = "0.1.0-SNAPSHOT"

dependencies {
  implementation(kotlin("stdlib-js"))

  implementation(project(":limber-backend-application:module:auth:auth-rest-interface"))
  implementation(project(":limber-backend-application:module:forms:forms-rest-interface"))
  implementation(project(":limber-backend-application:module:orgs:orgs-rest-interface"))
  implementation(project(":limber-backend-application:module:users:users-rest-interface"))
  implementation(project(":piper:rest-interface"))
  implementation(project(":piper:serialization"))
  implementation(project(":piper:util"))
  implementation(Dependencies.Kotlin.extensions)
  implementation(Dependencies.Kotlinx.coroutinesJs)
  implementation(Dependencies.KotlinJs.react)
  implementation(Dependencies.KotlinJs.reactDom)
  implementation(Dependencies.KotlinJs.reactRouterDom)
  implementation(Dependencies.KotlinJs.styledComponents)
  implementation(npm("@auth0/auth0-spa-js", "1.6.5"))
  implementation(npm("jsonwebtoken", "8.5.1"))
}

kotlin.target.browser {}
kotlin.target.useCommonJs()

detekt {
  config = files("$projectDir/.detekt/config.yml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
