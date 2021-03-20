plugins {
  id("limber-jvm-library")
}

dependencies {
  implementation(project(":limber-backend:common:util"))
  implementation(project(":limber-backend:common:validation"))
  api(project(":limber-backend:common:type-conversion:interface"))
}
