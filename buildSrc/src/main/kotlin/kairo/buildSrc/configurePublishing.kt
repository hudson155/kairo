package kairo.buildSrc

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get

internal fun Project.configurePublishing() {
  extensions.configure<PublishingExtension> {
    publications {
      repositories {
        maven {
          url = uri("artifactregistry://us-central1-maven.pkg.dev/kairo-13/kairo-13")
        }
      }
      publications {
        create<MavenPublication>("maven") {
          groupId = "kairo"
          artifactId = this@configurePublishing.name
          version = "0.1.0"

          from(components["java"])
        }
      }
    }
  }
}
