plugins {
  id("limber-jvm-library")
}

dependencies {
  api(project(":limber-backend:common:jwt"))
  api(project(":limber-backend:common:module"))
  api(project(":limber-backend:common:reps"))
  api(project(":limber-backend:common:rest-interface"))
  api(Dependencies.Ktor.auth)
  api(Dependencies.Ktor.serverCore)
  testImplementation(Dependencies.Ktor.serverTest)
}
