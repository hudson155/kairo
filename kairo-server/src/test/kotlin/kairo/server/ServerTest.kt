package kairo.server

import io.kotest.core.spec.style.FunSpec
import io.mockk.spyk
import io.mockk.verifyOrder
import kairo.feature.Feature
import kairo.feature.FeaturePriority

/**
 * This is a fairly basic test for [Server].
 * It has some basic assertions on its interaction with [Feature]s,
 * but is by no means comprehensive.
 *
 * Specifically, it doesn't test shutdown hooks, the wait flag
 */
internal class ServerTest : FunSpec({
  test("server") {
    class TestFeature : Feature() {
      override val name: String = "TestFeature"

      override val priority: FeaturePriority = FeaturePriority.Normal
    }

    val featureManagerConfig = FeatureManagerConfig(
      lifecycle = FeatureManagerConfig.Lifecycle(
        startupDelayMs = 0,
        shutdownDelayMs = 0,
      ),
    )
    val testFeature = spyk(TestFeature())
    val server = object : Server() {
      override val featureManager: FeatureManager =
        FeatureManager(features = setOf(testFeature), config = featureManagerConfig)
    }

    server.start(wait = false)
    server.shutDown()

    verifyOrder {
      testFeature.start(any(), eq(setOf(testFeature)))
      testFeature.afterStart(any())
      testFeature.beforeStop(any())
      testFeature.stop(any())
    }
  }
})
