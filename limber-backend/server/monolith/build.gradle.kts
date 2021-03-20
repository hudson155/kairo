plugins {
  id("limber-jvm-server")
}

dependencies {
  implementation(project(":limber-backend:common:server"))
  implementation(project(":limber-backend:common:sql"))
  implementation(project(":limber-backend:db:limber"))
  implementation(project(":limber-backend:module:auth:module"))
  implementation(project(":limber-backend:module:health-check:module"))
  implementation(project(":limber-backend:module:orgs:module"))
  implementation(project(":limber-backend:module:users:module"))
}
