plugins {
  id("limber-jvm-library")
}

dependencies {
  api(project(":limber-backend:common:permissions"))
  implementation(Dependencies.Jackson.annotations)
  testImplementation(project(":limber-backend:common:serialization"))
}
