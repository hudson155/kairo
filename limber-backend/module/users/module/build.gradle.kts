dependencies {
  implementation(project(":limber-backend:common:feature"))
  implementation(project(":limber-backend:common:sql"))
  api(project(":limber-backend:module:users:interface"))
  testImplementation(project(":limber-backend:common:integration-testing"))
  testImplementation(project(":limber-backend:common:sql:testing"))
  testImplementation(project(":limber-backend:db:limber"))
  testImplementation(project(":limber-backend:module:users:client"))
}