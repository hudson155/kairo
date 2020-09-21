plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(project(":limber-backend:common:testing-old"))
  api(project(":limber-backend:monolith"))
  api(project(":limber-backend:monolith:common:module"))
  implementation(Dependencies.Jwt.auth0JavaJwt)
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
