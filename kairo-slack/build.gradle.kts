plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-protected-string")) // In interface.

  api(libs.slack)
  api(libs.slack.kotlin)
}
