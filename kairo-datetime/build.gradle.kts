plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(libs.datetime)

  testImplementation(project(":kairo-testing"))
}
