plugins {
  id("limber-jvm")
}

dependencies {
  api(Dependencies.JavaExpressionLanguage.expressly)
  api(Dependencies.Ktor.httpJvm)
  api(Dependencies.Validation.hibernate)
}
