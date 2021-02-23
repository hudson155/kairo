package io.limberapp.testing.integration

import io.ktor.server.testing.TestApplicationEngine
import io.limberapp.server.Server
import org.junit.jupiter.api.extension.ExtensionContext

internal val TEST_CONTEXT: Map<Class<out Any>, String> = listOf(
    Server::class,
    TestApplicationEngine::class,
).associate { Pair(it.java, checkNotNull(it.qualifiedName)) }

@Suppress("UNCHECKED_CAST")
internal operator fun <T> ExtensionContext.set(key: Class<T>, value: Any): T =
    root.getStore(ExtensionContext.Namespace.GLOBAL).put(TEST_CONTEXT[key], value) as T

@Suppress("UNCHECKED_CAST")
internal operator fun <T> ExtensionContext.get(key: Class<T>): T =
    root.getStore(ExtensionContext.Namespace.GLOBAL).get(TEST_CONTEXT[key]) as T
