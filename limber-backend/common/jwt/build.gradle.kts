dependencies {
  api(project(":limber-multiplatform:permissions"))
  testImplementation(Dependencies.Jackson.databind)
  testImplementation(Dependencies.Jackson.moduleKotlin)
}
