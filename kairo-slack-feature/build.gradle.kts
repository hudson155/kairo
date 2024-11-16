plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-feature"))
  implementation(project(":kairo-logging"))

  api(libs.slackClient) // Exposed for clients.
  api(libs.slackClientKotlin) // Exposed for clients.

  testImplementation(project(":kairo-feature:testing"))
  testImplementation(project(":kairo-logging:testing"))
}
