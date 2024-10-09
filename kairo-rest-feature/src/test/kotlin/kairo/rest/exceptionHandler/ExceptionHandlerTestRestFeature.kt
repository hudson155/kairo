package kairo.rest.exceptionHandler

import kairo.rest.KairoRestConfig
import kairo.rest.KairoRestFeature
import kairo.restTesting.TestKairoRestFeature

internal const val exceptionHandlerTestRestPort: Int = 8081

/**
 * This test Feature intentionally extends [KairoRestFeature], not [TestKairoRestFeature].
 * In order for [ExceptionHandler]s to be tested properly, the entirety of Ktor's REST handling needs to be invoked.
 */
internal class ExceptionHandlerTestRestFeature : KairoRestFeature(
  config = KairoRestConfig(
    connector = KairoRestConfig.Connector(
      host = "0.0.0.0",
      port = exceptionHandlerTestRestPort,
    ),
    lifecycle = KairoRestConfig.Lifecycle(
      shutdownGracePeriodMs = 25_000, // 25 seconds.
      shutdownTimeoutMs = 15_000, // 15 seconds.
    ),
    parallelism = KairoRestConfig.Parallelism(
      connectionGroupSize = 4,
      workerGroupSize = 8,
      callGroupSize = 16,
    ),
  ),
  authVerifiers = emptyList(),
)
