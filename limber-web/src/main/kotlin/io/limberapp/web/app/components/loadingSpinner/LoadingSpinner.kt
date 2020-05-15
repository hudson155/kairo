package io.limberapp.web.app.components.loadingSpinner

import io.limberapp.web.app.components.inlineIcon.inlineIcon
import io.limberapp.web.util.Styles
import io.limberapp.web.util.globalStyles
import kotlinx.css.*
import react.*
import react.dom.*
import styled.getClassName

/**
 * Spinner to show while something is loading. Automatically centers itself horizontally.
 */
internal fun RBuilder.loadingSpinner() {
  div(classes = styles.getClassName { it::container }) {
    inlineIcon("spinner", classes = globalStyles.getClassName { it::spinner })
  }
}

private val styles = object : Styles("LoadingSpinner") {
  val container by css {
    padding(12.px)
    fontSize = 24.px
    textAlign = TextAlign.center
  }
}.apply { inject() }
