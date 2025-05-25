import org.jreleaser.model.Active
import org.jreleaser.model.Signing

plugins {
  java
  `maven-publish`
  id("org.jreleaser")
}

private val stagingDirectory: Directory
  get() = layout.buildDirectory.dir("staging-deploy").get()

java {
  withJavadocJar()
  withSourcesJar()
}

publishing {
  repositories {
    maven("localStaging") {
      url = stagingDirectory.asFile.toURI()
    }
  }
  publications {
    create<MavenPublication>("maven") {
      from(components["java"])
      groupId = "software.airborne.kairo"
      /**
       * Derives the artifact ID from the project path.
       */
      artifactId = run {
        val regex = Regex(":([a-z]+(-[a-z]+)*(:[a-z]+(-[a-z]+)*)?)")
        val path = project.path
        val match = requireNotNull(regex.matchEntire(path)) { "Invalid project name: $path." }
        return@run match.groupValues[1].replace(':', '-')
      }
      version = project.version.toString()
      pom {
        name = "Kairo"
        description = "Kairo is an application framework built for Kotlin."
        url = "https://github.com/hudson155/kairo"
        licenses {
          license {
            name = "Apache-2.0"
            url = "https://www.apache.org/licenses/LICENSE-2.0"
            distribution = "repo"
          }
        }
        developers {
          developer {
            id = "hudson155"
            name = "Jeff Hudson"
            email = "jeffryderhudson@gmail.com"
          }
        }
        scm {
          connection = "scm:git:git://github.com/hudson155/kairo.git"
          developerConnection = "scm:git:ssh://github.com/hudson155/kairo.git"
          url = "https://github.com/hudson155/kairo"
        }
      }
    }
  }
}

jreleaser {
  enabled = true
  dryrun = false
  gitRootSearch = true
  strict = true
  signing {
    active = Active.ALWAYS
    armored = true
    verify = true
    mode = Signing.Mode.COMMAND
  }
  deploy {
    maven {
      mavenCentral {
        register("central") {
          active = Active.ALWAYS
          url = "https://central.sonatype.com/api/v1/publisher"
          stagingRepository(stagingDirectory.toString())
        }
      }
    }
  }
}
