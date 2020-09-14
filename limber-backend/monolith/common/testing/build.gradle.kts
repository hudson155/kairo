plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(project(":limber-backend:common:testing"))
  api(project(":limber-backend:monolith"))
  api(project(":limber-backend:monolith:common:module"))
  implementation(Dependencies.Jwt.auth0JavaJwt)
}

detekt {
  config = files("$rootDir/.detekt/config.yml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
