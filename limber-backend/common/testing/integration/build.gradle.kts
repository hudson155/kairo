dependencies {
  api(project(":limber-backend:common:client"))
  api(project(":limber-backend:common:server"))
  api(project(":limber-backend:common:testing:unit"))
  implementation(Dependencies.Jwt.auth0JavaJwt)
  api(Dependencies.Ktor.test)
}
