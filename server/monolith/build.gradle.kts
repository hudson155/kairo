plugins {
  id("limber-jvm")
  id("limber-jvm-server")
}

dependencies {
  implementation(project(":common:server"))

  implementation(project(":feature:health-check:feature"))
  implementation(project(":feature:organization:feature"))
  implementation(project(":feature:rest:feature"))
  implementation(project(":feature:sql:feature"))

  implementation(project(":db:limber"))

  testImplementation(project(":common:feature:testing"))
}
