plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-serialization"))

  api(libs.serialization.json)
  api(libs.slack) // Available for usage.
  api(libs.slack.kotlin) // Available for usage.
}
