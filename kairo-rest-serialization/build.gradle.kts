plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-serialization"))

  api(libs.ktorSerializationJackson)
}
