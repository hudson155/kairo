plugins {
    kotlin("jvm")
    id(Plugins.detekt)
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
}
tasks.compileTestKotlin {
    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":limber-backend-application"))
    implementation(project(":limber-backend-application:common"))
    api(project(":piper:testing"))
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
}
