plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-feature"))
  implementation(project(":kairo-google-common"))
  implementation(project(":kairo-logging"))
  implementation(project(":kairo-rest-feature")) // Peer dependency.
  implementation(project(":kairo-transaction-manager")) // Optional peer dependency.

  implementation(libs.gcpCloudTasks)
}
