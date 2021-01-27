dependencies {
  api(project(":limber-backend:common:config"))
  implementation(project(":limber-backend:common:serialization"))
  implementation(project(":limber-backend:common:type-conversion:interface"))
  api(Dependencies.Jwt.auth0JavaJwt)
  implementation(Dependencies.Ktor.serverCore)
  testImplementation(project(":limber-backend:common:type-conversion:implementation"))
}
