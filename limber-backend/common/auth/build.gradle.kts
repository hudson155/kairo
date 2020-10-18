plugins {
  kotlin("jvm")
  id(Plugins.detekt)
}

dependencies {
  api(project(":limber-backend:common:permissions"))
  implementation(project(":limber-backend:common:serialization"))

  api(Dependencies.Jwt.auth0JavaJwt)
}

detekt {
  config = files("$rootDir/.detekt/config.yaml")
  input = files("src/main/kotlin", "src/test/kotlin")
}
