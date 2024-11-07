package kairo.googleCloudScheduler

import kairo.feature.Feature
import kairo.feature.FeaturePriority

public abstract class BaseKairoGoogleCloudSchedulerFeature : Feature() {
  final override val name: String = "Google Cloud Scheduler"

  final override val priority: FeaturePriority = FeaturePriority.Framework
}
