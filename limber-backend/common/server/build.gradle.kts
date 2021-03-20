plugins {
  id("limber-jvm-library")
}

dependencies {
  api(project(":limber-backend:common:client"))
  api(project(":limber-backend:common:config"))
  implementation(project(":limber-backend:common:exceptions"))
  implementation(project(":limber-backend:common:feature"))
  implementation(project(":limber-backend:common:jwt"))
  implementation(project(":limber-backend:common:module"))
  implementation(project(":limber-backend:common:serialization"))
  implementation(Dependencies.Google.guice)
  api(Dependencies.Jwt.auth0JavaJwt)
  implementation(Dependencies.Jwt.auth0JwksRsa)
  implementation(Dependencies.Ktor.auth)
  implementation(Dependencies.Ktor.jackson)
  api(Dependencies.Ktor.serverCio)
  implementation(Dependencies.Ktor.serverCore)
  implementation(Dependencies.Ktor.serverHostCommon)
  testImplementation(project(":limber-backend:common:client"))
  testImplementation(project(":limber-backend:common:type-conversion:implementation"))
  testImplementation(Dependencies.Ktor.serverTest)
}
