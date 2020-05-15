package io.limberapp.web.app.pages.unauthenticatedPage

import io.limberapp.web.app.components.layout.components.centeredContentLayout.centeredContentLayout
import io.limberapp.web.app.pages.signInPage.SignInPage
import io.limberapp.web.util.Styles
import kotlinx.css.*
import kotlinx.css.properties.*
import react.*
import react.dom.*
import react.router.dom.*
import styled.getClassName

/**
 * The only page shown when in an unauthenticated state.
 */
internal fun RBuilder.unauthenticatedPage() {
  child(component)
}

private val styles = object : Styles("UnauthenticatedPage") {
  val container by css {
    textAlign = TextAlign.center
  }
  val signInLink by css {
    textDecoration(TextDecorationLine.underline)
  }
}.apply { inject() }

private val component = functionalComponent<RProps> {
  centeredContentLayout {
    div(classes = styles.getClassName { it::container }) {
      h1 { +"Welcome to Limber" }
      navLink<RProps>(to = SignInPage.path, exact = true) {
        span(classes = styles.getClassName { it::signInLink }) { +"Click here to sign in" }
      }
    }
  }
}
