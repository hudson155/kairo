import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper

plugins {
  kotlin("multiplatform")
}

allprojects {
  apply<KotlinMultiplatformPluginWrapper>()

  kotlin {
    js(IR) {
      browser {
        binaries.executable()
      }
    }
    jvm()

    sourceSets {
      val commonMain by getting
      val commonTest by getting {
        dependencies {
          implementation(kotlin("test-annotations-common"))
          implementation(kotlin("test-common"))
        }
      }
      val jsMain by getting
      val jsTest by getting {
        dependencies {
          implementation(kotlin("test-js"))
        }
      }
      val jvmMain by getting {
        dependencies {
          implementation(kotlin("reflect"))
        }
      }
      val jvmTest by getting {
        dependencies {
          implementation(kotlin("test-junit5"))
          runtimeOnly(Dependencies.JUnit.engine)
        }
      }
    }
  }
}
