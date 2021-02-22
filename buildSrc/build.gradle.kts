plugins {
  `kotlin-dsl`
}

repositories {
  mavenCentral()
  // https://jfrog.com/blog/into-the-sunset-bintray-jcenter-gocenter-and-chartcenter/.
  jcenter() // JCenter is EOL.
}
