package io.limberapp.common.module

import com.google.inject.Guice
import com.google.inject.Inject
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.common.restInterface.Endpoint
import io.limberapp.common.restInterface.EndpointHandler
import io.limberapp.common.restInterface.template
import kotlin.reflect.KClass
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class FeatureTest {
  private interface TestInterface

  private class TestInterfaceImpl : TestInterface

  internal object TestEndpoint1 : Endpoint(HttpMethod.Get, "/endpoint1")

  internal object TestEndpoint2 : Endpoint(HttpMethod.Get, "/endpoint2")

  private class TestEndpoint1Handler @Inject constructor(
      @Suppress("UNUSED_PARAMETER") testInterface: TestInterface,
  ) : EndpointHandler<TestEndpoint1, Unit>(
      template = TestEndpoint1::class.template(),
  ) {
    override suspend fun endpoint(call: ApplicationCall): TestEndpoint1 = TestEndpoint1
    override suspend fun Handler.handle(endpoint: TestEndpoint1): Unit = Unit
  }

  private class TestEndpoint2Handler @Inject constructor(
      @Suppress("UNUSED_PARAMETER") testInterface: TestInterface,
  ) : EndpointHandler<TestEndpoint2, Unit>(
      template = TestEndpoint2::class.template(),
  ) {
    override suspend fun endpoint(call: ApplicationCall): TestEndpoint2 = TestEndpoint2
    override suspend fun Handler.handle(endpoint: TestEndpoint2): Unit = Unit
  }

  private inner class TestFeature : Feature() {
    override val apiEndpoints: List<KClass<out EndpointHandler<*, *>>> = listOf(
        TestEndpoint1Handler::class,
        TestEndpoint2Handler::class,
    )

    override fun bind() {
      bind(TestInterface::class.java).to(TestInterfaceImpl::class.java).asEagerSingleton()
      cleanedUp = false
    }

    override fun cleanUp() {
      cleanedUp = true
    }
  }

  private var cleanedUp: Boolean = true

  @BeforeTest
  fun before() {
    cleanedUp = true
  }

  @Test
  fun `without feature`() {
    assertTrue(cleanedUp)
    val injector = Guice.createInjector()
    assertTrue(cleanedUp)
    assertFails { injector.getInstance(TestInterface::class.java) }
    assertFails { injector.getInstance(TestEndpoint1Handler::class.java) }
    assertFails { injector.getInstance(TestEndpoint2Handler::class.java) }
  }

  @Test
  fun `with feature`() {
    assertTrue(cleanedUp)
    val testFeature = TestFeature()
    val injector = Guice.createInjector(testFeature)
    assertFalse(cleanedUp)
    injector.getInstance(TestInterface::class.java) // Just making sure this doesn't throw.
    injector.getInstance(TestEndpoint1Handler::class.java) // Just making sure this doesn't throw.
    injector.getInstance(TestEndpoint2Handler::class.java) // Just making sure this doesn't throw.
    testFeature.cleanUp()
    assertTrue(cleanedUp)
  }
}
