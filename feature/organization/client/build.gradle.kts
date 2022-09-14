plugins {
  id("limber-jvm")
}

dependencies {
  api(project(":feature:organization:interface"))
  api(project(":feature:rest:client"))
}
