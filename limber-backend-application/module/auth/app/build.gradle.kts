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
    api(project(":limber-backend-application:module:auth:api"))
    implementation(project(":limber-backend-application:common"))
    implementation(project(":limber-backend-application:common:api"))
    implementation(project(":limber-backend-application:module:orgs:api"))
    implementation(project(":limber-backend-application:module:users:api"))
    implementation(project(":piper:sql"))
    implementation(Dependencies.Bcrypt.jbcrypt)
    testImplementation(project(":limber-backend-application:common:testing"))
    testImplementation(project(":piper:sql:testing"))
}

detekt {
    config = files("$rootDir/.detekt/config.yml")
}
