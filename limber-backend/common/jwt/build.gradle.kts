dependencies {
  api(project(":limber-multiplatform:permissions"))
  implementation(Dependencies.Jackson.annotations)
  testImplementation(project(":limber-backend:common:serialization"))
}
