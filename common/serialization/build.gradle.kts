plugins {
  id("limber-jvm")
}

dependencies {
  api(Dependencies.Jackson.databind)
  implementation(Dependencies.Jackson.DataFormat.yaml)
  implementation(Dependencies.Jackson.DataType.jsr310)
  implementation(Dependencies.Jackson.Module.kotlin)
}
