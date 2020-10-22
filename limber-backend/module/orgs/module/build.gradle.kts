dependencies {
  implementation(project(":limber-backend:common:module"))
  implementation(project(":limber-backend:common:reps"))
  implementation(project(":limber-backend:common:serialization"))
  implementation(project(":limber-backend:common:sql"))
  api(project(":limber-backend:module:orgs:interface"))
  testImplementation(project(":limber-backend:common:sql:testing"))
  testImplementation(project(":limber-backend:common:testing:integration"))
  testImplementation(project(":limber-backend:module:orgs:client"))
  testImplementation(project(":limber-backend:server:monolith"))
}
