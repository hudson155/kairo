dependencies {
  implementation(project(":limber-backend:common:module"))
  implementation(project(":limber-backend:common:reps"))
  implementation(project(":limber-backend:common:serialization"))
  implementation(project(":limber-backend:common:sql"))
  implementation(project(":limber-backend:module:orgs:client"))
  api(project(":limber-backend:module:users:interface"))
  testImplementation(project(":limber-backend:common:sql:testing"))
  testImplementation(project(":limber-backend:common:testing:integration"))
  testImplementation(project(":limber-backend:module:users:client"))
  testImplementation(project(":limber-backend:server:monolith"))
}
