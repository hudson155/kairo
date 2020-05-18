package io.limberapp.web.app.pages.unauthenticatedPage

import io.limberapp.web.app.components.layout.components.centeredContentLayout.centeredContentLayout
import io.limberapp.web.app.pages.signInPage.SignInPage
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import kotlinx.css.*
import kotlinx.css.properties.*
import react.*
import react.dom.*
import react.router.dom.*

/**
 * The only page shown when in an unauthenticated state.
 */
internal fun RBuilder.unauthenticatedPage() {
  child(component)
}

private val s = object : Styles("UnauthenticatedPage") {
  val container by css {
    textAlign = TextAlign.center
  }
  val signInLink by css {
    textDecoration(TextDecorationLine.underline)
  }
}.apply { inject() }

private val component = functionalComponent<RProps> {
  centeredContentLayout {
    div(classes = s.c { it::container }) {
      h1 { +"Welcome to Limber" }
      navLink<RProps>(to = SignInPage.path, exact = true) {
        span(classes = s.c { it::signInLink }) { +"Click here to sign in" }
      }
    }
  }
}
