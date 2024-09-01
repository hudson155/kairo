package kairo.serverTesting

import kairo.feature.Feature
import kairo.server.FeatureManager
import kairo.server.FeatureManagerConfig
import kairo.server.Server

public abstract class TestServer(
  featureUnderTest: Feature,
  supportingFeatures: Set<Feature>,
) : Server() {
  final override val featureManager: FeatureManager =
    FeatureManager(
      features = buildSet {
        add(featureUnderTest)
        addAll(supportingFeatures)
      },
      config = FeatureManagerConfig(
        lifecycle = FeatureManagerConfig.Lifecycle(
          startupDelayMs = 0,
          shutdownDelayMs = 0,
        ),
      ),
    )
}
