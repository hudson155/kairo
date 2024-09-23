plugins {
  id("kairo")
  id("kairo-publish")
}

dependencies {
  api(libs.ktorHttpJvm) // For [HttpStatusCode].
}
