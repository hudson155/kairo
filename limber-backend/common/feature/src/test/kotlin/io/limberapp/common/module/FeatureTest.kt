package io.limberapp.common.module

import com.google.inject.Guice
import io.limberapp.backend.endpoint.test.RequiredQueryParamFooEndpointHandler
import io.limberapp.backend.endpoint.test.SingletonEndpointHandler
import io.limberapp.backend.service.test.TestService
import io.limberapp.backend.service.test.TestServiceImpl
import io.limberapp.common.restInterface.EndpointHandler
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class FeatureTest {
  private inner class TestFeature : Feature() {
    override val apiEndpoints: List<KClass<out EndpointHandler<*, *>>> = listOf(
        SingletonEndpointHandler::class,
        RequiredQueryParamFooEndpointHandler::class,
    )

    override fun bind() {
      bind(TestService::class.java).to(TestServiceImpl::class.java).asEagerSingleton()
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
  fun `without feature`() {
    assertTrue(cleanedUp)
    val injector = Guice.createInjector()
    assertTrue(cleanedUp)
    assertFails { injector.getInstance(TestService::class.java) }
    assertFails { injector.getInstance(SingletonEndpointHandler::class.java) }
    assertFails { injector.getInstance(RequiredQueryParamFooEndpointHandler::class.java) }
  }

  @Test
  fun `with feature`() {
    assertTrue(cleanedUp)
    val testFeature = TestFeature()
    val injector = Guice.createInjector(testFeature)
    assertFalse(cleanedUp)
    injector.getInstance(TestService::class.java) // Just making sure this doesn't throw.
    injector.getInstance(SingletonEndpointHandler::class.java) // Just making sure this doesn't throw.
    injector.getInstance(RequiredQueryParamFooEndpointHandler::class.java) // Just making sure this doesn't throw.
    testFeature.cleanUp()
    assertTrue(cleanedUp)
  }
}
