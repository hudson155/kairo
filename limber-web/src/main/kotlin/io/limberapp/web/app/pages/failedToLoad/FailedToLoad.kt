package io.limberapp.web.app.pages.failedToLoad

import io.limberapp.web.app.components.inlineIcon.inlineIcon
import io.limberapp.web.app.pages.failedToLoadPage.failedToLoadPage
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import kotlinx.css.*
import react.*
import react.dom.*
import styled.getClassName

/**
 * "Failed to load" error message. When some component's data fails to load, this should be rendered instead of throwing
 * an exception or using [failedToLoadPage]. This component just renders some text indicating that loading failed,
 * rather than causing the entire app to not be renderable.
 *
 * [entityName] is the name of the thing that failed to load. It should start with a lowercase letter, and may or may
 * not be plural.
 */
internal fun RBuilder.failedToLoad(entityName: String) {
  div(classes = s.getClassName { it::container }) {
    inlineIcon("exclamation-triangle", rightMargin = true, classes = s.getClassName { it::icon })
    +"Something went wrong loading the $entityName. Please refresh the page to try again."
  }
}

private class S : Styles("FailedToLoad") {
  val container by css {
    textAlign = TextAlign.center
    padding(top = 8.px, horizontal = 8.px, bottom = 0.px)
  }
  val icon by css {
    color = Theme.Color.Text.red
  }
}

private val s = S().apply { inject() }
