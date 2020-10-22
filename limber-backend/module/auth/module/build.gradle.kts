dependencies {
  implementation(project(":limber-backend:common:module"))
  implementation(project(":limber-backend:common:reps"))
  implementation(project(":limber-backend:common:serialization"))
  implementation(project(":limber-backend:common:sql"))
  api(project(":limber-backend:module:auth:interface"))
  implementation(project(":limber-backend:module:orgs:client"))
  implementation(project(":limber-backend:module:users:client"))
  implementation(Dependencies.Bcrypt.jbcrypt)
  testImplementation(project(":limber-backend:common:sql:testing"))
  testImplementation(project(":limber-backend:common:testing:integration"))
  testImplementation(project(":limber-backend:module:auth:client"))
  testImplementation(project(":limber-backend:server:monolith"))
}
