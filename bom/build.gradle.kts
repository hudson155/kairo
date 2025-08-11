plugins {
  id("kairo")
}

dependencies {
  api(platform(libs.gcpBom))
  api(platform(libs.guavaBom))
  api(platform(libs.kotlinxCoroutinesBom))
  api(platform(libs.kotlinxSerializationBom))
  api(platform(libs.log4jBom))
}
