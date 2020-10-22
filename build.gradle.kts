plugins {
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
