package io.limberapp.web.app.pages.featurePage.pages.homePage

import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.components.layout.components.standardLayout.standardLayout
import react.*
import react.dom.*

internal fun RBuilder.homePage(feature: FeatureRep.Complete) {
  child(component, Props(feature))
}

internal data class Props(val feature: FeatureRep.Complete) : RProps

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  standardLayout {
    layoutTitle(props.feature.name)
    p { +"This is the homepage." }
  }
}
