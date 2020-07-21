package io.limberapp.web.app.pages.loadingPage

import io.limberapp.web.app.components.layout.components.centeredContentLayout.centeredContentLayout
import io.limberapp.web.app.components.loadingSpinner.loadingSpinner
import react.*
import react.dom.*

/**
 * Page to show while things are loading.
 */
internal fun RBuilder.loadingPage(loadingText: String) {
  child(component, Props(loadingText))
}

internal data class Props(val loadingText: String) : RProps

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  centeredContentLayout {
    loadingSpinner(large = true)
    p { +props.loadingText }
  }
}
