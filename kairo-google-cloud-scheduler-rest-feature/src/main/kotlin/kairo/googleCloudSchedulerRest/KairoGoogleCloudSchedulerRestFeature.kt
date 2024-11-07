package kairo.googleCloudSchedulerRest

import com.google.inject.Binder
import kairo.feature.Feature
import kairo.feature.FeaturePriority
import kairo.rest.server.bindRestHandlers

public open class KairoGoogleCloudSchedulerRestFeature : Feature() {
  final override val name: String = "Google Cloud Scheduler REST"

  final override val priority: FeaturePriority = FeaturePriority.Normal

  override fun bind(binder: Binder) {
    binder.bindRestHandlers<GoogleCloudSchedulerHandler>()
  }
}
