dependencies {
  api(project(":limber-backend:common:rest-interface"))
  api(project(":limber-backend:common:serialization"))
  api(Dependencies.Google.guice)
  implementation(Dependencies.Ktor.clientCio)
  implementation(Dependencies.Ktor.clientJackson)
  testImplementation(Dependencies.Ktor.clientMock)
}
