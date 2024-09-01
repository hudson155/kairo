plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-feature:testing"))
  api(project(":kairo-rest-feature"))
}
