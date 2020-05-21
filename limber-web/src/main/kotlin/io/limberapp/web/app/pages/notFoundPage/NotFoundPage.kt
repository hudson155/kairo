package io.limberapp.web.app.pages.notFoundPage

import io.limberapp.web.app.components.layout.components.centeredContentLayout.centeredContentLayout
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import kotlinx.css.*
import react.*
import react.dom.*

/**
 * "Not found" error message page when no page is found.
 */
internal fun RBuilder.notFoundPage() {
  child(component)
}

internal object NotFoundPage {
  const val name = "Not Found"
}

private class S : Styles("NotFoundPage") {
  val container by css {
    textAlign = TextAlign.center
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent<RProps> {
  centeredContentLayout {
    div(classes = s.c { it::container }) {
      h1 { +NotFoundPage.name }
      p { +"We looked everywhere, but we couldn't find the page you were looking for." }
    }
  }
}
