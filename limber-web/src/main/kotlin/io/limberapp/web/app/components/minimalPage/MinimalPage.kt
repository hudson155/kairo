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
 */
internal fun RBuilder.minimalPage(
  linkType: LinkType?,
  children: RBuilder.() -> Unit
) {
  child(component, Props(linkType), handler = children)
}

internal data class Props(val linkType: LinkType?) : RProps

internal enum class LinkType { SIGN_IN, SIGN_OUT }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  page(
    header = buildElement {
      basicNavbar {
        when (props.linkType) {
          LinkType.SIGN_IN -> {
            navLink<RProps>(to = SignInPage.path, exact = true) { headerItem { +SignInPage.name } }
          }
          LinkType.SIGN_OUT -> {
            navLink<RProps>(to = SignOutPage.path, exact = true) { headerItem { +SignOutPage.name } }
          }
        }
      }
    },
    footer = buildElement { footer() }
  ) {
    props.children()
  }
}
