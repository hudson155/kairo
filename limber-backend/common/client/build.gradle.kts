dependencies {
  api(project(":limber-backend:common:exception-mapping"))
  api(project(":limber-backend:common:rest-interface"))
  api(project(":limber-backend:common:serialization"))
  api(Dependencies.Google.guice) // Not used by this module - provided to consumers.
  implementation(Dependencies.Ktor.clientCio)
}
