plugins {
  id("limber-jvm")
}

dependencies {
  api(Dependencies.Jackson.databind)
  implementation(Dependencies.Jackson.DataFormat.yaml)
  implementation(Dependencies.Jackson.DataType.jsr310)
  api(Dependencies.Jackson.Module.kotlin) // Exposed for access to extension functions like [readValue].
}
