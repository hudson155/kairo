package io.limberapp.framework.testing

import com.google.inject.AbstractModule
import io.mockk.mockkClass
import kotlin.reflect.KClass

class MockedServices(servicesToMock: List<KClass<*>>) : AbstractModule() {

    constructor(vararg servicesToMock: KClass<*>) : this(servicesToMock.toList())

    private val mocks = servicesToMock.associateWith { mockkClass(it) }

    override fun configure() {
        mocks.forEach { bindMock(it.key) }
    }

    private fun <T : Any> bindMock(clazz: KClass<T>) {
        bind(clazz.java).toInstance(get(clazz))
    }

    @Suppress("UNCHECKED_CAST", "UnsafeCast")
    operator fun <T : Any> get(clazz: KClass<T>) = mocks[clazz] as T
}
