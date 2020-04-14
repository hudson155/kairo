plugins {
    kotlin("js")
    id(Plugins.detekt).version(Versions.detekt)
}

group = "io.limberapp.web"
version = "0.1.0-SNAPSHOT"

repositories {
    maven(url = "https://kotlin.bintray.com/kotlin-js-wrappers/")
}

dependencies {

    implementation(kotlin("stdlib-js"))

    implementation(project(":limber-backend-application:module:auth:auth-rest-interface"))
    implementation(project(":limber-backend-application:module:forms:forms-rest-interface"))
    implementation(project(":limber-backend-application:module:orgs:orgs-rest-interface"))
    implementation(project(":limber-backend-application:module:users:users-rest-interface"))

    implementation("org.jetbrains:kotlin-react:16.13.0-pre.94-kotlin-1.3.70")
    implementation(npm("react", "16.13.0"))

    implementation("org.jetbrains:kotlin-react-dom:16.13.0-pre.94-kotlin-1.3.70")
    implementation(npm("react-dom", "16.13.0"))

    implementation("org.jetbrains:kotlin-react-router-dom:4.3.1-pre.94-kotlin-1.3.70")
    implementation(npm("react-router-dom", "4.3.1"))

    implementation("org.jetbrains:kotlin-styled:1.0.0-pre.94-kotlin-1.3.70")
    implementation(npm("styled-components", "5.1.0"))
    implementation(npm("inline-style-prefixer", "6.0.0"))

    implementation(npm("@auth0/auth0-spa-js", "1.6.5"))

    implementation(Dependencies.Kotlinx.coroutinesCommon)
}

kotlin.target.browser {}
kotlin.target.useCommonJs()

detekt {
    config = files("$projectDir/.detekt/config.yml")
    input = files("src/main/kotlin", "src/test/kotlin")
}
