plugins {
  id("limber-jvm-library")
}

dependencies {
  api(Dependencies.Jackson.databind)
  testImplementation(project(":limber-backend:common:serialization"))
}
