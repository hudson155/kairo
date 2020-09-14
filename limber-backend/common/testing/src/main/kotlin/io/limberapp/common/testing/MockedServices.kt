package io.limberapp.common.testing

import io.limberapp.common.module.ModuleWithLifecycle
import io.mockk.mockkClass
import kotlin.reflect.KClass

class MockedServices(servicesToMock: List<KClass<*>>) : ModuleWithLifecycle() {
  constructor(vararg servicesToMock: KClass<*>) : this(servicesToMock.toList())

  private val mocks = servicesToMock.associateWith { mockkClass(it) }

  override fun configure() {
    mocks.forEach { bindMock(it.key) }
  }

  override fun unconfigure() = Unit

  private fun <T : Any> bindMock(kClass: KClass<T>) {
    bind(kClass.java).toInstance(get(kClass))
  }

  @Suppress("UNCHECKED_CAST", "UnsafeCast")
  operator fun <T : Any> get(kClass: KClass<T>) = mocks[kClass] as T
}
