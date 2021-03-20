plugins {
  id("limber-jvm-library")
}

dependencies {
  api(project(":limber-backend:common:feature"))
  implementation(project(":limber-backend:common:sql"))
  api(project(":limber-backend:module:auth:interface"))
  implementation(project(":limber-backend:module:orgs:client"))
  implementation(project(":limber-backend:module:users:client"))
  implementation(Dependencies.Bcrypt.jbcrypt)
  testImplementation(project(":limber-backend:common:integration-testing"))
  testImplementation(project(":limber-backend:common:sql:testing"))
  testImplementation(project(":limber-backend:db:limber"))
  testImplementation(project(":limber-backend:module:auth:client"))
}
