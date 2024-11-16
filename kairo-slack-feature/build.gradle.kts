plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(project(":kairo-feature"))
  implementation(project(":kairo-logging"))

  implementation(libs.kotlinxCoroutinesCore)
  api(libs.slackClient) // Exposed for clients.
  api(libs.slackClientKotlin) // Exposed for clients.
}
