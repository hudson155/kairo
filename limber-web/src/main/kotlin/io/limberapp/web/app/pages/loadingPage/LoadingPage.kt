package io.limberapp.web.app.pages.loadingPage

import io.limberapp.web.app.components.inlineIcon.inlineIcon
import io.limberapp.web.app.components.layout.components.centeredContentLayout.centeredContentLayout
import io.limberapp.web.util.Styles
import io.limberapp.web.util.globalStyles
import kotlinx.css.*
import react.*
import react.dom.*
import styled.getClassName

/**
 * Page to show while things are loading.
 */
internal fun RBuilder.loadingPage(loadingText: String) {
  child(component, Props(loadingText))
}

internal data class Props(val loadingText: String) : RProps

private val styles = object : Styles("LoadingPage") {
  val spinnerContainer by css {
    fontSize = 48.px
  }
}.apply { inject() }

private val component = functionalComponent<Props> { props ->
  centeredContentLayout {
    div(classes = styles.getClassName { it::spinnerContainer }) {
      inlineIcon("spinner", classes = globalStyles.getClassName { it::spinner })
    }
    p { +props.loadingText }
  }
}
