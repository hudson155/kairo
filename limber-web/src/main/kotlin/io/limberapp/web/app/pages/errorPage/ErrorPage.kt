package io.limberapp.web.app.pages.errorPage

import io.limberapp.web.app.components.inlineIcon.inlineIcon
import io.limberapp.web.app.components.layout.components.centeredContentLayout.centeredContentLayout
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import io.limberapp.web.util.component
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
  val icon by css {
    color = Theme.Color.Text.red
  }
}

private val s = S().apply { inject() }

private val component = component<Props> component@{ props ->
  // TODO: Add error logging in this component, and in other places in the app.
  centeredContentLayout {
    div(classes = s.c { it::container }) {
      h1 { inlineIcon("exclamation-triangle", classes = s.c { it::icon }) }
      h1 { +props.title }
      p { +props.description }
    }
  }
}
