plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  implementation(project(":kairo-dependency-injection"))
  compileOnly(project(":kairo-feature"))
  api(project(":kairo-gcp-pubsub"))
}
