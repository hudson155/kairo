plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(project(":limber-backend:common:client"))
  api(project(":limber-backend:common:server"))
  api(project(":limber-backend:common:testing:unit"))

  implementation(Dependencies.Jwt.auth0JavaJwt)
  api(Dependencies.Ktor.test)
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
