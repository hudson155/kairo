plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  compileOnly(project(":kairo-gcp-pubsub"))

  testImplementation(project(":kairo-gcp-pubsub"))
  testImplementation(project(":kairo-testing"))
}
