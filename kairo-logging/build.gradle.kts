plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(libs.logging)
  implementation(libs.log4j.configyaml)
  implementation(libs.log4j.core)
  implementation(libs.log4j.layouttemplatejson)
  implementation(libs.log4j.slf4j)
}
