plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-feature"))
  implementation(project(":kairo-google-common"))
  implementation(project(":kairo-logging"))
  implementation(project(":kairo-rest-feature"))
  implementation(project(":kairo-transaction-manager"))

  implementation(libs.gcpCloudTasks)
}
