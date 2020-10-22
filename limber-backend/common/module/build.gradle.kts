dependencies {
  api(project(":limber-backend:common:auth"))
  api(project(":limber-backend:common:config"))
  api(project(":limber-backend:common:exceptions"))
  api(project(":limber-backend:common:permissions"))
  implementation(project(":limber-backend:common:reps"))
  api(project(":limber-backend:common:rest-interface"))
  api(project(":limber-backend:common:serialization"))
  api(Dependencies.Google.guice)
  api(Dependencies.Ktor.auth)
  api(Dependencies.Ktor.serverCore)
}
