package kairo.googleAppEngine

import com.google.inject.Binder
import kairo.feature.Feature
import kairo.feature.FeaturePriority
import kairo.rest.server.bindRestHandlers

public open class KairoGoogleAppEngineFeature : Feature() {
  final override val name: String = "Google App Engine"

  final override val priority: FeaturePriority = FeaturePriority.Normal

  override fun bind(binder: Binder) {
    binder.bindRestHandlers<GoogleAppEngineHandler>()
  }
}
