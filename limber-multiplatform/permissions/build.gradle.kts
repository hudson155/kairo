kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(project(":limber-multiplatform:darb"))
      }
    }
    jvmMain {
      dependencies {
        api(Dependencies.Jackson.databind)
      }
    }
    jvmTest {
      dependencies {
        implementation(Dependencies.Jackson.moduleKotlin)
      }
    }
  }
}
