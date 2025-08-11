plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":bom"))

  api(project(":kairo-command-runner"))

  testImplementation(project(":kairo-testing"))
}
