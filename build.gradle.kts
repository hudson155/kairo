plugins {
  kotlin("multiplatform") version Versions.kotlin apply false
  id(Plugins.detekt) version Versions.detekt apply false
  idea
}

allprojects {
  repositories {
    jcenter()
  }
}

tasks.create("downloadDependencies") {
  description = "Download all dependencies"
  doLast {
    configurations.forEach { if (it.isCanBeResolved) it.resolve() }
  }
}
