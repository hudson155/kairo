plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-logging"))
  api(project(":kairo-protected-string"))

  implementation(libs.gcpSecretManager)
  implementation(libs.guava)
  implementation(libs.kotlinxCoroutinesGuava)
}
