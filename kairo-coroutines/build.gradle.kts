plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(libs.arrowCoroutines)
  api(libs.kotlinxCoroutinesCore)
}
