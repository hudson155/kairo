plugins {
  id("com.google.cloud.artifactregistry.gradle-plugin")
}

repositories {
  mavenLocal()
  mavenCentral()
  artifactRegistry()
}
