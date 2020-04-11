plugins {
    kotlin("js")
}

group = "io.limberapp.web"
version = "0.0.0"

repositories {
    maven(url = "https://kotlin.bintray.com/kotlin-js-wrappers/")
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation(npm("@auth0/auth0-spa-js", "1.6.5"))
    implementation(project(":limber-backend-application:module:auth:auth-rest-interface"))
    implementation(project(":limber-backend-application:module:forms:forms-rest-interface"))
    implementation(project(":limber-backend-application:module:orgs:orgs-rest-interface"))
    implementation(project(":limber-backend-application:module:users:users-rest-interface"))

    //React, React DOM + Wrappers (chapter 3)
    implementation("org.jetbrains:kotlin-react:16.13.0-pre.93-kotlin-1.3.70")
    implementation("org.jetbrains:kotlin-react-dom:16.13.0-pre.93-kotlin-1.3.70")
    implementation(npm("react", "16.12.0"))
    implementation(npm("react-dom", "16.12.0"))

    // React Router
    implementation("org.jetbrains:kotlin-react-router-dom:4.3.1-pre.94-kotlin-1.3.70")
    implementation(npm("react-router-dom", "4.3.1"))

    //Kotlin Styled (chapter 3)
    implementation("org.jetbrains:kotlin-styled:1.0.0-pre.94-kotlin-1.3.70")
    implementation(npm("styled-components"))
    implementation(npm("inline-style-prefixer"))

    //Video Player (chapter 7)
    implementation(npm("react-player"))

    //Share Buttons (chapter 7)
    implementation(npm("react-share"))

    //Coroutines (chapter 8)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:1.3.5")
}

kotlin.target.browser {
    webpackTask {
        this.configDirectory
    }
}
