kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(project(":limber-multiplatform:darb"))
      }
    }
    jvmMain {
      dependencies {
        api(project(":limber-backend:common:type-conversion:interface"))
      }
    }
  }
}
