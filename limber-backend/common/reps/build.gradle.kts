plugins {
  id("limber-jvm-library")
}

dependencies {
  api(project(":limber-backend:common:validation"))
  api(Dependencies.Jackson.annotations)
}
