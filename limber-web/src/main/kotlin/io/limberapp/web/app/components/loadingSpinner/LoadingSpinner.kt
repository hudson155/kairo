package io.limberapp.web.app.components.loadingSpinner

import io.limberapp.web.app.components.inlineIcon.inlineIcon
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import io.limberapp.web.util.gs
import kotlinx.css.*
import react.*
import react.dom.*

/**
 * Spinner to show while something is loading. Automatically centers itself horizontally.
 */
internal fun RBuilder.loadingSpinner() {
  div(classes = s.c { it::container }) {
    inlineIcon("spinner", classes = gs.c { it::spinner })
  }
}

private val s = object : Styles("LoadingSpinner") {
  val container by css {
    padding(12.px)
    fontSize = 24.px
    textAlign = TextAlign.center
  }
}.apply { inject() }
