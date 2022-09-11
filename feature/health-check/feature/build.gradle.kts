plugins {
  id("limber-jvm")
}

dependencies {
  api(project(":feature:health-check:client"))
  api(project(":feature:rest:feature"))
}
