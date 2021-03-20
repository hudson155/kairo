plugins {
  id("limber-jvm-library")
}

dependencies {
  api(project(":limber-backend:common:feature"))
  api(project(":limber-backend:module:health-check:interface"))
  testImplementation(project(":limber-backend:common:integration-testing"))
  testImplementation(project(":limber-backend:module:health-check:client"))
}
