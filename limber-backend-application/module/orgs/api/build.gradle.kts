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
    implementation(Dependencies.Jackson.annotations)
    implementation(project(":limber-backend-application:common:api"))
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
}
