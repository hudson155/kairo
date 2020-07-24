package io.limberapp.web.app.pages.featurePage

import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.formsFeaturePage
import io.limberapp.web.app.pages.featurePage.pages.homePage.homePage
import io.limberapp.web.app.pages.notFoundPage.notFoundPage
import io.limberapp.web.state.state.feature.featureStateProvider
import react.*

internal fun RBuilder.featurePage(feature: FeatureRep.Complete) {
  child(component, Props(feature))
}

internal data class Props(val feature: FeatureRep.Complete) : RProps

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  featureStateProvider(props.feature) {
    when (props.feature.type) {
      FeatureRep.Type.FORMS -> formsFeaturePage()
      FeatureRep.Type.HOME -> homePage()
      else -> notFoundPage()
    }
  }
}
