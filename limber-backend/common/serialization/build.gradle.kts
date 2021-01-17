dependencies {
  api(project(":limber-backend:common:type-conversion:interface"))
  api(Dependencies.Jackson.databind)
  implementation(Dependencies.Jackson.dataformatYaml)
  implementation(Dependencies.Jackson.datatypeJsr310)
  api(Dependencies.Jackson.moduleKotlin)
  testImplementation(project(":limber-backend:common:type-conversion:implementation"))
}
