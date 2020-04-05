package com.piperframework.testing

import com.piperframework.module.ModuleWithLifecycle
import io.mockk.mockkClass
import kotlin.reflect.KClass

class MockedServices(servicesToMock: List<KClass<*>>) : ModuleWithLifecycle() {

    constructor(vararg servicesToMock: KClass<*>) : this(servicesToMock.toList())

    private val mocks = servicesToMock.associateWith { mockkClass(it) }

    override fun configure() {
        mocks.forEach { bindMock(it.key) }
    }

    override fun unconfigure() = Unit

    private fun <T : Any> bindMock(klass: KClass<T>) {
        bind(klass.java).toInstance(get(klass))
    }

    @Suppress("UNCHECKED_CAST", "UnsafeCast")
    operator fun <T : Any> get(klass: KClass<T>) = mocks[klass] as T
}
