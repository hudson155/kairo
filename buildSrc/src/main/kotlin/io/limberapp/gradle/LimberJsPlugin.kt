package io.limberapp.gradle

internal val DEFAULT_JS_DEPENDENCIES: List<Dependency> = emptyList()

internal val DEFAULT_JS_TEST_DEPENDENCIES: List<Dependency> = listOf(
    Dependency.Kotlin(
        configuration = Dependency.Configuration.IMPLEMENTATION,
        dependencyNotation = "test-js",
    )
)
