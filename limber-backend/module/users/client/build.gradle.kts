plugins {
  id("limber-jvm-library")
}

dependencies {
  api(project(":limber-backend:common:client"))
  api(project(":limber-backend:module:users:interface"))
}
