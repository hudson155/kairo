package kairo.slack

import kairo.feature.Feature
import kairo.feature.FeaturePriority

public abstract class BaseKairoSlackFeature : Feature() {
  final override val name: String = "Slack"

  final override val priority: FeaturePriority = FeaturePriority.Normal
}
