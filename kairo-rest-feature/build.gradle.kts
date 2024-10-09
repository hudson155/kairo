plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-exception")) // Exposed for clients.
  api(project(":kairo-feature"))
  implementation(project(":kairo-logging"))
  api(project(":kairo-protected-string"))
  implementation(project(":kairo-reflect"))
  api(project(":kairo-serialization")) // Exposed for clients.

  api(libs.auth0JavaJwt)
  api(libs.auth0JwksRsa)
  api(libs.ktorHttpJvm) // Exposed for clients.
  implementation(libs.ktorSerializationJackson)
  implementation(libs.ktorServerAuth)
  api(libs.ktorServerCio)
  implementation(libs.ktorServerContentNegotiation)
  api(libs.ktorServerCore)
  implementation(libs.ktorServerStatusPages)

  testImplementation(project(":kairo-rest-client"))
  testImplementation(project(":kairo-feature:testing"))
  testImplementation(project(":kairo-id-feature"))
  testImplementation(project(":kairo-logging:testing"))
  testImplementation(project(":kairo-rest-feature:testing"))
  testImplementation(project(":kairo-testing"))
}
