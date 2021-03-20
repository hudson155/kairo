plugins {
  id("limber-jvm-library")
}

dependencies {
  api(project(":limber-backend:common:permissions"))
  api(project(":limber-backend:common:rest-interface"))
}
