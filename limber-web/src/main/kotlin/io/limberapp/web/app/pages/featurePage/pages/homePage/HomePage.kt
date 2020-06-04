package io.limberapp.web.app.pages.featurePage.pages.homePage

import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.components.layout.components.standardLayout.standardLayout
import io.limberapp.web.app.pages.featurePage.Props
import io.limberapp.web.util.component
import react.*
import react.dom.*

/**
 * Placeholder homepage.
 */
internal fun RBuilder.homePage(feature: FeatureRep.Complete) {
  child(component, Props(feature))
}

internal data class Props(val feature: FeatureRep.Complete) : RProps

private val component = component<Props> component@{ props ->
  standardLayout {
    layoutTitle(props.feature.name)
    p { +"This is the homepage." }
  }
}
