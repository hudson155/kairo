plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  compileOnly(project(":kairo-exception"))
  compileOnly(project(":kairo-testing"))

  testImplementation(project(":kairo-exception"))
  testImplementation(project(":kairo-testing"))
}
