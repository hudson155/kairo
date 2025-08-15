plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(project(":kairo-command-runner"))

  testImplementation(project(":kairo-testing"))
}
