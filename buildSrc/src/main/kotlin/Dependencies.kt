object Dependencies {

    private object Versions {

        // https://github.com/auth0/java-jwt/releases
        const val auth0JavaJwt = "3.9.0"

        // https://github.com/auth0/jwks-rsa-java/releases
        const val auth0JwksRsa = "0.9.0"

        // https://github.com/arturbosch/detekt/releases
        const val detekt = "1.3.1"

        // https://mvnrepository.com/artifact/org.jetbrains.exposed/exposed
        const val exposed = "0.17.7"

        // https://github.com/flyway/flyway/releases
        const val flyway = "6.1.4"

        // https://github.com/google/guice/releases
        const val guice = "4.2.2"

        // https://github.com/brettwooldridge/HikariCP/releases
        const val hikari = "3.4.2"

        // https://github.com/FasterXML/jackson-core/releases
        const val jackson = "2.10.2"

        // https://mvnrepository.com/artifact/org.mindrot/jbcrypt
        const val jbcrypt = "0.4"

        // https://github.com/junit-team/junit5/releases
        const val junit = "5.5.2"

        // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-stdlib-jdk8
        const val kotlin = "1.3.61"

        // https://github.com/ktorio/ktor/releases
        const val ktor = "1.2.5"

        // https://github.com/qos-ch/logback/releases
        const val logback = "1.2.3"

        // https://github.com/mockk/mockk/releases
        const val mockK = "1.9.3"

        // https://mvnrepository.com/artifact/org.postgresql/postgresql
        const val postgres = "42.2.9"

        // https://github.com/johnrengelman/shadow/releases
        const val shadow = "5.1.0"
    }

    object Kotlin {
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
        const val stdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
        const val test = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}"
        const val testJunit5 = "org.jetbrains.kotlin:kotlin-test-junit5:${Versions.kotlin}"
    }

    object Detekt {
        const val gradlePlugin = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.detekt}"
    }

    object Ktor {
        const val auth = "io.ktor:ktor-auth:${Versions.ktor}"
        const val httpJvm = "io.ktor:ktor-http-jvm:${Versions.ktor}"
        const val jackson = "io.ktor:ktor-jackson:${Versions.ktor}"
        const val serverCio = "io.ktor:ktor-server-cio:${Versions.ktor}"
        const val serverCore = "io.ktor:ktor-server-core:${Versions.ktor}"
        const val serverHostCommon = "io.ktor:ktor-server-host-common:${Versions.ktor}"
        const val test = "io.ktor:ktor-server-test-host:${Versions.ktor}"
    }

    object Bcrypt {
        const val jbcrypt = "org.mindrot:jbcrypt:${Versions.jbcrypt}"
    }

    object Jwt {
        const val auth0JavaJwt = "com.auth0:java-jwt:${Versions.auth0JavaJwt}"
        const val auth0JwksRsa = "com.auth0:jwks-rsa:${Versions.auth0JwksRsa}"
    }

    object Jackson {
        const val annotations = "com.fasterxml.jackson.core:jackson-annotations:${Versions.jackson}"
        const val dataFormatYaml = "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:${Versions.jackson}"
        const val dataTypeJsr310 = "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${Versions.jackson}"
        const val moduleKotlin = "com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.jackson}"
    }

    object Guice {
        const val guice = "com.google.inject:guice:${Versions.guice}"
    }

    object Logback {
        const val logbackClassic = "ch.qos.logback:logback-classic:${Versions.logback}"
    }

    object Sql {
        const val exposed = "org.jetbrains.exposed:exposed:${Versions.exposed}"
        const val flyway = "org.flywaydb:flyway-core:${Versions.flyway}"
        const val hikari = "com.zaxxer:HikariCP:${Versions.hikari}"
        const val postgres = "org.postgresql:postgresql:${Versions.postgres}"
    }

    object Shadow {
        const val shadow = "com.github.jengelman.gradle.plugins:shadow:${Versions.shadow}"
    }

    object JUnit {
        const val api = "org.junit.jupiter:junit-jupiter-api:${Versions.junit}"
        const val engine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit}"
    }

    object MockK {
        const val mockK = "io.mockk:mockk:${Versions.mockK}"
    }
}
