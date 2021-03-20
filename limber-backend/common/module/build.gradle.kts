plugins {
  id("limber-jvm-library")
}

dependencies {
  api(project(":limber-backend:common:exceptions"))
  implementation(project(":limber-backend:common:permissions"))
  api(project(":limber-backend:common:serialization"))
  implementation(project(":limber-backend:common:type-conversion:implementation"))
  api(project(":limber-backend:common:type-conversion:interface"))
  api(project(":limber-backend:common:util"))
  api(Dependencies.Google.guice)
}
