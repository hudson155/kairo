package io.limberapp.web.app.pages.unauthenticatedPage

import io.limberapp.web.app.components.layout.components.centeredContentLayout.centeredContentLayout
import io.limberapp.web.app.pages.signInPage.SignInPage
import io.limberapp.web.state.state.tenant.useTenantState
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import kotlinx.css.*
import kotlinx.css.properties.*
import react.*
import react.dom.*
import react.router.dom.*

internal fun RBuilder.unauthenticatedPage() {
  child(component)
}

internal typealias Props = RProps

private class S : Styles("UnauthenticatedPage") {
  val container by css {
    textAlign = TextAlign.center
  }
  val signInLink by css {
    textDecoration(TextDecorationLine.underline)
  }
  val fatBottom by css {
    marginBottom = 64.px
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val (tenant, _) = useTenantState()
  centeredContentLayout {
    div(classes = s.c { it::container }) {
      h1 { +"Welcome to ${tenant.name}" }
      p(classes = s.c { it::fatBottom }) { +"Powered by Limber" }
      navLink<RProps>(to = SignInPage.path, exact = true) {
        span(classes = s.c { it::signInLink }) { +"Click here to sign in" }
      }
    }
  }
}
