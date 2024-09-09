plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-logging"))
  api(project(":kairo-protected-string"))

  implementation(libs.gcpSecretManager)
}
