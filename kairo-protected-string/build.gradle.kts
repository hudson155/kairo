plugins {
  id("kairo-library")
  id("kairo-library-publish")
  id("kairo-serialization")
}

dependencies {
  api(platform(project(":bom")))

  implementation(libs.kotlinxSerializationCore)

  testImplementation(project(":kairo-testing"))

  implementation(libs.kotlinxSerializationJson)
}
