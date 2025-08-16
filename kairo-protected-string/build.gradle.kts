plugins {
  id("kairo-library")
  id("kairo-library-publish")
  id("kairo-serialization")
}

dependencies {
  implementation(libs.kotlinxSerializationCore)

  testImplementation(project(":kairo-testing"))

  implementation(libs.kotlinxSerializationJson)
}
