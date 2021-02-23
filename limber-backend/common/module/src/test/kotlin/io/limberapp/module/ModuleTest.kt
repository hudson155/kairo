package io.limberapp.module

import com.google.inject.Guice
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class ModuleTest {
  private interface TestInterface

  private class TestInterfaceImpl : TestInterface

  private inner class TestModule : Module() {
    override fun bind() {
      bind(TestInterface::class.java).to(TestInterfaceImpl::class.java).asEagerSingleton()
      cleanedUp = false
    }

    override fun cleanUp() {
      cleanedUp = true
    }
  }

  private var cleanedUp: Boolean = true

  @BeforeEach
  fun before() {
    cleanedUp = true
  }

  @Test
  fun `without module`() {
    assertTrue(cleanedUp)
    val injector = Guice.createInjector()
    assertTrue(cleanedUp)
    assertFails { injector.getInstance(TestInterface::class.java) }
  }

  @Test
  fun `with module`() {
    assertTrue(cleanedUp)
    val testModule = TestModule()
    val injector = Guice.createInjector(testModule)
    assertFalse(cleanedUp)
    injector.getInstance(TestInterface::class.java) // Just making sure this doesn't throw.
    testModule.cleanUp()
    assertTrue(cleanedUp)
  }
}
