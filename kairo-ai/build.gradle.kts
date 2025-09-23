// TODO: Split this out into a separate repo. BOM.

plugins {
  kotlin("plugin.serialization")
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-coroutines"))
  implementation(project(":kairo-serialization"))

  api(libs.langchain4j.core)
}
