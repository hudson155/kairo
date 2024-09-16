plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-feature:testing"))
  implementation(project(":kairo-logging"))
  api(project(":kairo-sql-feature"))
}
