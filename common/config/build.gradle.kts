plugins {
  id("limber-jvm")
}

dependencies {
  implementation(project(":common:util"))
  api(project(":common:type:protected-string")) // Make this type available to library users.
  implementation(Dependencies.Gcp.secretManager)
  api(Dependencies.Jackson.databind) // Make the necessary annotations available to library users.

  testImplementation(project(":common:serialization"))
}
