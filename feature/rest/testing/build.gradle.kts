plugins {
  id("limber-jvm")
}

dependencies {
  api(project(":common:feature:testing"))
  api(project(":feature:rest:feature"))
}
