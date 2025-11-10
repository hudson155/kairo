plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-serialization")) // Available for usage.

  api(libs.serialization.json) // Available for usage.
  api(libs.slack) // Available for usage.
  api(libs.slack.kotlin) // Available for usage.
}
