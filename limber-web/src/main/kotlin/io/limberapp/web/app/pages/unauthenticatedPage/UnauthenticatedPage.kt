package io.limberapp.web.app.pages.unauthenticatedPage

import io.limberapp.web.app.components.layout.components.centeredContentLayout.centeredContentLayout
import io.limberapp.web.app.pages.signInPage.SignInPage
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import io.limberapp.web.util.component
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

private class S : Styles("UnauthenticatedPage") {
  val container by css {
    textAlign = TextAlign.center
  }
  val signInLink by css {
    textDecoration(TextDecorationLine.underline)
  }
}

private val s = S().apply { inject() }

private val component = component<RProps> component@{ _ ->
  centeredContentLayout {
    div(classes = s.c { it::container }) {
      h1 { +"Welcome to Limber" }
      navLink<RProps>(to = SignInPage.path, exact = true) {
        span(classes = s.c { it::signInLink }) { +"Click here to sign in" }
      }
    }
  }
}
