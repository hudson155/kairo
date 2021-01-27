dependencies {
  api(project(":limber-backend:common:config"))
  implementation(project(":limber-backend:common:serialization"))
  api(Dependencies.Jwt.auth0JavaJwt)
}
