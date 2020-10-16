plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(project(":limber-backend:common:config"))
  api(project(":limber-backend:common:exception-mapping"))
  implementation(project(":limber-backend:common:ktor-auth"))
  api(project(":limber-backend:common:module"))
  api(project(":limber-backend:common:reps"))
  api(project(":limber-backend:common:serialization"))
  implementation(project(":limber-backend:common:type-conversion"))
  api(project(":limber-backend:common:util"))

  api(project(":limber-backend:deprecated:common:module"))

  implementation(Dependencies.Google.guice)
  implementation(Dependencies.Jackson.dataFormatYaml)
  implementation(Dependencies.Jwt.auth0JavaJwt)
  implementation(Dependencies.Jwt.auth0JwksRsa)
  implementation(Dependencies.Ktor.jackson)
  api(Dependencies.Ktor.serverCore)
  implementation(Dependencies.Ktor.serverHostCommon)
  implementation(Dependencies.Logging.slf4j)
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
