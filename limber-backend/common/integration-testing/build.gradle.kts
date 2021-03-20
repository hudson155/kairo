plugins {
  id("limber-jvm-library")
}

dependencies {
  api(project(":limber-backend:common:client"))
  implementation(project(":limber-backend:common:jwt"))
  api(project(":limber-backend:common:module"))
  api(project(":limber-backend:common:server"))
  api(Dependencies.JUnit.api)
  implementation(Dependencies.Jwt.auth0JavaJwt)
  api(Dependencies.Ktor.serverTest)
  api(Dependencies.MockK.mockK)
}
