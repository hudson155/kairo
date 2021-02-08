dependencies {
  api(project(":limber-backend:common:exceptions"))
  api(project(":limber-backend:common:serialization"))
  implementation(project(":limber-backend:common:type-conversion:implementation"))
  api(project(":limber-backend:common:type-conversion:interface"))
  implementation(project(":limber-multiplatform:permissions"))
  api(Dependencies.Google.guice)
}
