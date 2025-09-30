plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  testImplementation(project(":kairo-testing"))
  testImplementation(project(":kairo-util"))

  testImplementation(libs.guava)
}
