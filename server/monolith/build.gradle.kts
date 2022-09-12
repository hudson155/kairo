plugins {
  id("limber-jvm")
  id("limber-jvm-server")
}

dependencies {
  implementation(project(":common:server"))

  implementation(project(":feature:health-check:feature"))
  implementation(project(":feature:rest:feature"))

  testImplementation(project(":common:feature:testing"))
}
