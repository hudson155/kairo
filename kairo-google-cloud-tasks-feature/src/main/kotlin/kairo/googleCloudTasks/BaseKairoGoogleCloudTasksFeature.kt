package kairo.googleCloudTasks

import kairo.feature.Feature
import kairo.feature.FeaturePriority

public abstract class BaseKairoGoogleCloudTasksFeature : Feature() {
  final override val name: String = "Google Cloud Tasks"

  final override val priority: FeaturePriority = FeaturePriority.Framework
}
