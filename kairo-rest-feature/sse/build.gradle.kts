plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-reflect"))
  implementation(project(":kairo-rest-feature")) // Peer dependency.

  implementation(libs.ktorServerSse)
}
