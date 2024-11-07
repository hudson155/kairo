plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-feature"))
  implementation(project(":kairo-google-cloud-scheduler-feature")) // Peer dependency.
  implementation(project(":kairo-google-cloud-tasks-feature")) // Peer dependency.
  implementation(project(":kairo-logging"))
  implementation(project(":kairo-rest-feature")) // Peer dependency.

  testImplementation(project(":kairo-feature:testing"))
  implementation(project(":kairo-google-cloud-scheduler-feature:testing"))
  implementation(project(":kairo-google-cloud-tasks-feature:testing"))
  testImplementation(project(":kairo-logging:testing"))
  testImplementation(project(":kairo-rest-feature:testing"))
}
