plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":bom"))

  implementation(project(":kairo-logging"))
  api(project(":kairo-protected-string"))

  implementation(libs.gcpSecretManager)
  implementation(libs.guava)
  implementation(libs.kotlinxCoroutinesCore)
  implementation(libs.kotlinxCoroutinesGuava)
}
