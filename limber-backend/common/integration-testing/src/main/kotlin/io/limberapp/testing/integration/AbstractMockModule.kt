package io.limberapp.testing.integration

import io.limberapp.module.Module
import io.mockk.mockk
import kotlin.reflect.KClass

abstract class AbstractMockModule : Module() {
  protected inline fun <reified T : Any> mock(kClass: KClass<T>) {
    with(kClass.java) {
      bind(this).toInstance(mockk())
      expose(this)
    }
  }

  final override fun cleanUp(): Unit = Unit
}
