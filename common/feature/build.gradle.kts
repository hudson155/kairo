plugins {
  id("limber-jvm")
}

dependencies {
  api(project(":common:util")) // Make utils available in all features.
  api(Dependencies.Google.guice) // Make Guice available in all features.
}
