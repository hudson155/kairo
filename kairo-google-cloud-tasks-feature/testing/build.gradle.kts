plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-feature:testing"))
  api(project(":kairo-google-cloud-tasks-feature"))
  implementation(project(":kairo-logging"))
  implementation(project(":kairo-rest-feature")) // Peer dependency.
}
