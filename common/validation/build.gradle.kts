plugins {
  id("limber-jvm")
}

dependencies {
  api(Dependencies.JavaExpressionLanguage.expressly)
  api(Dependencies.Validation.hibernate)
}
