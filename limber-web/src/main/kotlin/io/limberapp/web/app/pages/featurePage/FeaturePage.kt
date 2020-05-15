package io.limberapp.web.app.pages.featurePage

import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.formsFeaturePage
import io.limberapp.web.app.pages.featurePage.pages.homePage.homePage
import io.limberapp.web.app.pages.notFoundPage.notFoundPage
import react.*

/**
 * Parent page for feature-specific pages.
 */
internal fun RBuilder.featurePage(feature: FeatureRep.Complete) {
  child(component, Props(feature))
}

internal data class Props(val feature: FeatureRep.Complete) : RProps

private val component = functionalComponent<Props> { props ->
  when (props.feature.type) {
    FeatureRep.Type.FORMS -> formsFeaturePage()
    FeatureRep.Type.HOME -> homePage()
    else -> notFoundPage()
  }
}
