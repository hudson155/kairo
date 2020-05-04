package io.limberapp.web.app.pages.featurePage

import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.web.app.pages.featurePage.pages.formsFeaturePage.formsFeaturePage
import io.limberapp.web.app.pages.featurePage.pages.homePage.homePage
import io.limberapp.web.app.pages.notFoundPage.notFoundPage
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

/**
 * Parent page for feature-specific pages.
 */
internal fun RBuilder.featurePage(feature: FeatureRep.Complete) {
    child(featurePage, Props(feature))
}

internal data class Props(val feature: FeatureRep.Complete) : RProps

private val featurePage = functionalComponent<Props> { props ->
    when (props.feature.type) {
        FeatureRep.Type.FORMS -> formsFeaturePage()
        FeatureRep.Type.HOME -> homePage()
        else -> notFoundPage()
    }
}
