package io.limberapp.web.app.pages.loadingPage

import io.limberapp.web.app.components.layout.components.centeredContentLayout.centeredContentLayout
import io.limberapp.web.app.components.loadingSpinner.loadingSpinner
import io.limberapp.web.util.component
import react.*
import react.dom.*

/**
 * Page to show while things are loading.
 */
internal fun RBuilder.loadingPage(loadingText: String) {
  child(component, Props(loadingText))
}

internal data class Props(val loadingText: String) : RProps

private val component = component<Props> component@{ props ->
  centeredContentLayout {
    loadingSpinner(large = true)
    p { +props.loadingText }
  }
}
