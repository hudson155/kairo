plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  compileOnly(project(":kairo-dependency-injection"))
  compileOnly(project(":kairo-testing"))
}
