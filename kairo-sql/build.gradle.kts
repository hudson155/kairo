plugins {
  id("kairo-library")
  id("kairo-library-publish")
}

dependencies {
  api(libs.exposed) // Available for usage.
  api(libs.exposed.crypt) // Available for usage.
  api(libs.exposed.json) // Available for usage.
  api(libs.exposed.kotlinDatetime) // Available for usage.
  api(libs.exposed.money) // Available for usage.
  api(libs.exposed.r2dbc) // Available for usage.
}
