package io.limberapp.web.app.pages.errorPage

import io.limberapp.web.app.components.layout.components.centeredContentLayout.centeredContentLayout
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import kotlinx.css.*
import react.*
import react.dom.*

/**
 * Generic error page.
 */
internal fun RBuilder.errorPage(title: String, description: String) {
  child(component, Props(title, description))
}

internal data class Props(val title: String, val description: String) : RProps

private class S : Styles("ErrorPage") {
  val container by css {
    textAlign = TextAlign.center
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent<Props> { props ->
  // TODO: Add error logging in this component, and in other places in the app.
  centeredContentLayout {
    div(classes = s.c { it::container }) {
      h1 { +props.title }
      p { +props.description }
    }
  }
}
