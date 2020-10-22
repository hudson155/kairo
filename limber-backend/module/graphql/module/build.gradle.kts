dependencies {
  implementation(project(":limber-backend:common:module"))
  implementation(project(":limber-backend:common:reps"))
  api(project(":limber-backend:module:graphql:interface"))
  testImplementation(project(":limber-backend:common:testing:integration"))
  testImplementation(project(":limber-backend:module:graphql:client"))
}
