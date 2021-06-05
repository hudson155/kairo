plugins {
  id("limber-multiplatform-library")
}

kotlin {
  sourceSets {
    jvmMain {
      dependencies {
        api(project(":limber-backend:common:permissions"))
        implementation(Dependencies.Jackson.annotations)
      }
    }
    jvmTest {
      dependencies {
        implementation(project(":limber-backend:common:serialization"))
      }
    }
  }
}
