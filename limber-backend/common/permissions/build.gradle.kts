plugins {
  id("limber-multiplatform-library")
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(project(":limber-backend:common:darb"))
        implementation(project(":limber-backend:common:multiplatform-logging"))
      }
    }
    jvmMain {
      dependencies {
        api(project(":limber-backend:common:type-conversion:interface"))
      }
    }
  }
}
