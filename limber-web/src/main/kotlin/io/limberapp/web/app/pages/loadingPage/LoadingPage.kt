package io.limberapp.web.app.pages.loadingPage

import io.limberapp.web.app.components.inlineIcon.inlineIcon
import io.limberapp.web.app.components.layout.components.centeredContentLayout.centeredContentLayout
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import io.limberapp.web.util.component
import io.limberapp.web.util.gs
import kotlinx.css.*
import react.*
import react.dom.*

/**
 * Page to show while things are loading.
 */
internal fun RBuilder.loadingPage(loadingText: String) {
  child(component, Props(loadingText))
}

internal data class Props(val loadingText: String) : RProps

private class S : Styles("LoadingPage") {
  val spinnerContainer by css {
    fontSize = 48.px
  }
}

private val s = S().apply { inject() }

private val component = component<Props> component@{ props ->
  centeredContentLayout {
    div(classes = s.c { it::spinnerContainer }) {
      inlineIcon("spinner", classes = gs.c { it::spinner })
    }
    p { +props.loadingText }
  }
}
