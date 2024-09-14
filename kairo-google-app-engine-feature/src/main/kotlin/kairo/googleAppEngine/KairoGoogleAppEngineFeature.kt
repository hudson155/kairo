package kairo.googleAppEngine

import com.google.inject.Binder
import kairo.feature.Feature
import kairo.feature.FeaturePriority
import kairo.rest.bindRestHandlers

public open class KairoGoogleAppEngineFeature : Feature() {
  override val name: String = "Google App Engine"

  override val priority: FeaturePriority = FeaturePriority.Normal

  override fun bind(binder: Binder) {
    binder.bindRestHandlers<GoogleAppEngineHandler>()
  }
}
