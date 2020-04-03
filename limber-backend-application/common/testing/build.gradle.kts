plugins {
    kotlin("jvm")
    id(Plugins.detekt)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation(project(":limber-backend-application"))
    implementation(project(":limber-backend-application:common"))
    api(project(":piper:testing"))
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
}
