package limber.feature

import limber.config.RestConfig
import limber.rest.RestFeature

public class TestRestFeature(port: Int) : RestFeature(
  RestConfig(
    allowedHosts = emptyList(),
    parallelization = RestConfig.Parallelization(
      connectionGroupSize = 4,
      workerGroupSize = 8,
      callGroupSize = 16,
    ),
    port = port,
    shutDown = RestConfig.ShutDown(
      gracePeriodMillis = 10,
      timeoutMillis = 2000, // 2 seconds.
    ),
  ),
)
