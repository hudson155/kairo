plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(libs.googleApiCommon)
  implementation(libs.kotlinxCoroutinesCore)
}
