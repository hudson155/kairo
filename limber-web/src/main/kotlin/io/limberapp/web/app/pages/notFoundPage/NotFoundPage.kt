package io.limberapp.web.app.pages.notFoundPage

import io.limberapp.web.app.components.layout.components.centeredContentLayout.centeredContentLayout
import io.limberapp.web.util.Styles
import kotlinx.css.TextAlign
import kotlinx.css.textAlign
import react.RBuilder
import react.RProps
import react.child
import react.dom.div
import react.dom.h1
import react.dom.p
import react.functionalComponent
import styled.getClassName

/**
 * "Not found" error message page when no page is found.
 */
internal fun RBuilder.notFoundPage() {
  child(component)
}

internal object NotFoundPage {
  const val name = "Not Found"
}

private val styles = object : Styles("NotFoundPage") {
  val container by css {
    textAlign = TextAlign.center
  }
}.apply { inject() }

private val component = functionalComponent<RProps> {
  centeredContentLayout {
    div(classes = styles.getClassName { it::container }) {
      h1 { +NotFoundPage.name }
      p { +"We looked everywhere, but we couldn't find the page you were looking for." }
    }
  }
}
