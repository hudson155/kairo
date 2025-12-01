@file:Suppress("MatchingDeclarationName")

import java.net.URI
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.toolchain.JavaLanguageVersion

internal val javaVersion: JavaLanguageVersion = JavaLanguageVersion.of(25)

internal fun RepositoryHandler.artifactRegistry() {
  maven {
    url = URI("artifactregistry://us-central1-maven.pkg.dev/airborne-software/maven")
  }
}

internal fun groupId(): String =
  "software.airborne.kairo"

internal fun artifactId(path: String): String =
  path.trimStart(':').replace(':', '-')

internal fun MavenPublication.license() {
  pom {
    licenses {
      license {
        name.set("The Apache License, Version 2.0")
        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
        distribution.set("repo")
      }
    }
  }
}

internal fun requireVersion(version: Any) {
  require(version is String && version != "unspecified") { "Version is not specified" }
}
