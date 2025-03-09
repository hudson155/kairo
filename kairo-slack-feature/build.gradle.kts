plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  implementation(project(":kairo-coroutines"))
  api(project(":kairo-feature"))
  implementation(project(":kairo-logging"))

  api(libs.slackClient) // Exposed for clients.
  api(libs.slackClientKotlin) // Exposed for clients.
}
