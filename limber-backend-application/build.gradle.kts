import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.internal.HasConvention
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

buildscript {
    dependencies {
        classpath(Dependencies.Shadow.shadow)
    }
}

plugins {
    application
}
apply(plugin = "com.github.johnrengelman.shadow")

group = "io.limberapp.backend"
version = "0.0.0"
application {
    mainClassName = "io.limberapp.backend.ApplicationKt"
}

val SourceSet.kotlin: SourceDirectorySet get() = (this as HasConvention).convention.getPlugin<KotlinSourceSet>().kotlin
sourceSets {
    getByName("main").kotlin.srcDir("src/main/kotlin")
    getByName("main").java.srcDirs("src/main/kotlin")
    getByName("test").kotlin.srcDir("src/test/kotlin")
    getByName("test").java.srcDirs("src/test/kotlin")
    getByName("main").resources.srcDir("src/main/resources")
    getByName("test").resources.srcDir("src/test/resources")
}

dependencies {
    implementation(project(":limber-backend-application:common"))
    implementation(project(":limber-backend-application:module:auth:app"))
    implementation(project(":limber-backend-application:module:forms:app"))
    implementation(project(":limber-backend-application:module:orgs:app"))
    implementation(project(":limber-backend-application:module:users:app"))
    implementation(project(":piper"))
    implementation(project(":piper:sql"))
    api(Dependencies.Jwt.auth0JavaJwt)
    api(Dependencies.Jwt.auth0JwksRsa)
    implementation(Dependencies.Jackson.dataFormatYaml)
    implementation(Dependencies.Ktor.serverCio)
    implementation(Dependencies.Logback.logbackClassic)
}

tasks {
    named<ShadowJar>("shadowJar") {
        manifest {
            attributes(
                mapOf(
                    "MainClass" to application.mainClassName,
                    "archiveName" to "limber-backend.jar"
                )
            )
        }
    }
}
