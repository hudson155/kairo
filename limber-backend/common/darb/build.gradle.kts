plugins {
  id("limber-multiplatform-library")
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(project(":limber-backend:common:multiplatform-logging"))
      }
    }
  }
}
