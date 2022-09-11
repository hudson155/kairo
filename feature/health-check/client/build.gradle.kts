plugins {
  id("limber-jvm")
}

dependencies {
  api(project(":feature:health-check:interface"))
  api(project(":feature:rest:client"))
}
