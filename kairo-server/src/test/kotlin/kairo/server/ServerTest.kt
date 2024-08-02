package kairo.server

import io.kotest.core.spec.style.FunSpec
import io.mockk.spyk
import io.mockk.verifyOrder
import kairo.feature.Feature
import kairo.feature.FeaturePriority

internal class ServerTest : FunSpec({
  test("server") {
    class TestFeature : Feature() {
      override val name: String = "TestFeature"

      override val priority: FeaturePriority = FeaturePriority.Normal
    }

    val config = ServerConfig(lifecycle = ServerConfig.Lifecycle(startupDelayMs = 0, shutdownDelayMs = 0))
    val testFeature = spyk(TestFeature())
    val server = object : Server(config) {
      override val features: Set<Feature> =
        setOf(testFeature)
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
