package limber.feature.form

import com.google.inject.PrivateBinder
import limber.feature.Feature
import limber.feature.FeaturePriority
import limber.feature.rest.RestImplementation
import limber.feature.rest.bindHttpClient

public class FormFeature(private val rest: RestImplementation) : Feature() {
  override val priority: FeaturePriority = FeaturePriority.Normal

  override fun bind(binder: PrivateBinder) {
    binder.bindHttpClient(rest)
  }
}
