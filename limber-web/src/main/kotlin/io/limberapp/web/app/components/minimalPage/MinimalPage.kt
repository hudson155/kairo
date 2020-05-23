package io.limberapp.web.app.components.minimalPage

import io.limberapp.web.app.components.basicNavbar.basicNavbar
import io.limberapp.web.app.components.footer.footer
import io.limberapp.web.app.components.navbar.components.headerItem.headerItem
import io.limberapp.web.app.components.page.page
import io.limberapp.web.app.pages.signInPage.SignInPage
import io.limberapp.web.app.pages.signOutPage.SignOutPage
import react.*
import react.router.dom.*

/**
 * Renders a page with a basic navbar and the footer. This is useful for error pages and loading pages.
 *
 * The [children] is the main page content, same as for [page].
 */
internal fun RBuilder.minimalPage(
  withSignInLink: Boolean = false,
  withSignOutLink: Boolean = false,
  children: RBuilder.() -> Unit
) {
  child(component, Props(withSignInLink, withSignOutLink), handler = children)
}

internal data class Props(val withSignInLink: Boolean, val withSignOutLink: Boolean) : RProps {
  init {
    check(!withSignInLink || !withSignOutLink)
  }
}

private val component = functionalComponent<Props> { props ->
  page(
    header = buildElement {
      basicNavbar {
        if (props.withSignInLink) {
          navLink<RProps>(to = SignInPage.path, exact = true) { headerItem { +SignInPage.name } }
        }
        if (props.withSignOutLink) {
          navLink<RProps>(to = SignOutPage.path, exact = true) { headerItem { +SignOutPage.name } }
        }
      }
    },
    footer = buildElement { footer() }
  ) {
    props.children()
  }
}
