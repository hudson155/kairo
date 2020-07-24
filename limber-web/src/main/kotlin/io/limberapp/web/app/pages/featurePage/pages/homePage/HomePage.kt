package io.limberapp.web.app.pages.featurePage.pages.homePage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.components.layout.components.standardLayout.standardLayout
import io.limberapp.web.state.state.feature.useFeatureState
import react.*
import react.dom.*

internal fun RBuilder.homePage() {
  child(component)
}

internal typealias Props = RProps

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val (feature) = useFeatureState()

  standardLayout {
    layoutTitle(feature.name)
    p { +"This is the homepage." }
  }
}
