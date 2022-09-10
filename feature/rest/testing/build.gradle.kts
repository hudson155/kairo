plugins {
  id("limber-jvm")
}

dependencies {
  api(project(":common:feature:testing")) // Make general feature testing available to library users.
}
