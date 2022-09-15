plugins {
  id("limber-jvm")
}

dependencies {
  api(project(":feature:organization:interface"))
  implementation(project(":feature:organization:client"))

  implementation(project(":feature:rest:feature"))
  testImplementation(project(":feature:rest:testing"))

  implementation(project(":feature:sql:feature"))
  testImplementation(project(":feature:sql:testing"))
  testImplementation(project(":db:limber"))
}
