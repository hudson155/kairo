plugins {
  id("limber-jvm")
}

dependencies {
  api(Dependencies.Logging.kotlinLogging)
  implementation(Dependencies.Logging.Log4j.core)
  implementation(Dependencies.Logging.Log4j.layoutTemplateJson)
  implementation(Dependencies.Logging.Log4j.slf4jImpl)
}
