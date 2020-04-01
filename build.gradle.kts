import io.gitlab.arturbosch.detekt.Detekt

plugins {
    `kotlin-dsl`
}

buildscript {
    repositories {
        jcenter()
        maven(url = "https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath(Dependencies.Detekt.gradlePlugin)
        classpath(Dependencies.Kotlin.gradlePlugin)
    }
}

allprojects {

    apply(plugin = "kotlin")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    tasks.compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    tasks.compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    tasks.compileJava {
        options.encoding = "UTF-8"
    }
    tasks.compileTestJava {
        options.encoding = "UTF-8"
    }

    tasks.test {
        useJUnitPlatform()
        // Running tests via the command line does not log the result of each test.
        // This adds such logging, so that when tests fail on CI we can know which ones failed.
        addTestListener(object : TestListener {
            override fun afterSuite(suite: TestDescriptor?, result: TestResult?) = Unit
            override fun beforeSuite(suite: TestDescriptor?) = Unit
            override fun beforeTest(testDescriptor: TestDescriptor?) = Unit
            override fun afterTest(descriptor: TestDescriptor, result: TestResult) {
                println("Completed test: $descriptor")
                logger.quiet("Test ${result.resultType}: ${descriptor.className}::${descriptor.name}")
            }
        })
    }

    // This task downloads all external dependencies.
    // It"s highly useful on CI, especially when it comes to caching.
    // https://discuss.circleci.com/t/recommended-dependencies-caching-for-a-gradle-setup-is-wrong/20712
    tasks.register("downloadDependencies") {
        description = "Download all dependencies to the Gradle cache"
        doLast {
            configurations.filter { it.isCanBeResolved }.forEach { it.resolve() }
        }
    }

    tasks {
        named<Detekt>("detekt") {
            config.setFrom("$rootDir/.detekt/config.yml")
        }
    }
}

subprojects {

    buildscript {
        repositories {
            jcenter()
        }
    }

    repositories {
        jcenter()
    }

    dependencies {
        runtimeOnly(Dependencies.Kotlin.reflect)
        implementation(Dependencies.Kotlin.stdlibJdk8)
    }
}

repositories {
    jcenter()
}
