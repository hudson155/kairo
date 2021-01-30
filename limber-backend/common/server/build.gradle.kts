dependencies {
  api(project(":limber-backend:common:config"))
  implementation(project(":limber-backend:common:feature"))
  implementation(project(":limber-backend:common:serialization"))
  implementation(project(":limber-backend:common:type-conversion:interface"))
  implementation(Dependencies.Jwt.auth0JavaJwt)
  implementation(Dependencies.Jwt.auth0JwksRsa)
  implementation(Dependencies.Ktor.auth)
  implementation(Dependencies.Ktor.serverCore)
  testImplementation(project(":limber-backend:common:type-conversion:implementation"))
  testImplementation(Dependencies.Ktor.serverTest)
}
