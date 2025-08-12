plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(platform(project(":bom")))

  implementation(project(":kairo-logging"))
  api(project(":kairo-protected-string"))

  implementation(libs.gcpSecretManager)
  implementation(libs.guava)
  implementation(libs.kotlinxCoroutinesGuava)
}
