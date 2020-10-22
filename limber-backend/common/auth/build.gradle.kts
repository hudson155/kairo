dependencies {
  api(project(":limber-backend:common:permissions"))
  implementation(project(":limber-backend:common:serialization"))

  api(Dependencies.Jwt.auth0JavaJwt)
}
