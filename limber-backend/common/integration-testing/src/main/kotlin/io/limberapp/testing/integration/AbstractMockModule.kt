package io.limberapp.testing.integration

import io.limberapp.module.Module
import io.mockk.mockk
import kotlin.reflect.KClass

abstract class AbstractMockModule : Module() {
  protected inline fun <reified T : Any> mock(kClass: KClass<T>) {
    bind(kClass.java).toInstance(mockk())
  }

  final override fun cleanUp(): Unit = Unit
}
