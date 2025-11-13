plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(libs.slack) // Available for usage.
  api(libs.slack.kotlin) // Available for usage.
}
