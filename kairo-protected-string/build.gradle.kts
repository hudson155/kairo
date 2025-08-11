plugins {
  id("kairo")
  id("kairo-publish")
  id("kairo-serialization")
}

dependencies {
  implementation(project(":bom"))

  implementation(libs.kotlinxSerializationCore)

  testImplementation(project(":kairo-testing"))

  implementation(libs.kotlinxSerializationJson)
}
