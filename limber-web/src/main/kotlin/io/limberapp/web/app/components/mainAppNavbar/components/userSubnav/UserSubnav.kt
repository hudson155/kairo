package io.limberapp.web.app.components.mainAppNavbar.components.userSubnav

import io.limberapp.web.app.components.mainAppNavbar.mainAppNavbar
import io.limberapp.web.app.components.navbar.components.subnav.components.subnavGroup.subnavGroup
import io.limberapp.web.app.components.navbar.components.subnav.components.subnavItem.subnavItem
import io.limberapp.web.app.components.navbar.components.subnav.subnav
import io.limberapp.web.app.pages.orgSettingsPage.OrgSettingsPage
import io.limberapp.web.app.pages.signOutPage.SignOutPage
import io.limberapp.web.auth.useAuth
import io.limberapp.web.hook.useClickListener
import io.limberapp.web.state.state.user.useUserState
import react.*
import react.dom.*
import react.router.dom.*

/**
 * Subnav on the [mainAppNavbar] that shows up when the user's name/photo is clicked.
 *
 * [onUnfocus] is the function to call when there's a click outside of this element. Normally, calling this function
 * should hide the subnav.
 */
internal fun RBuilder.userSubnav(onUnfocus: () -> Unit) {
  child(component, Props(onUnfocus))
}

internal data class Props(val onUnfocus: () -> Unit) : RProps

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val auth = useAuth()

  val (user, _) = useUserState()

  val name = user.fullName

  useClickListener(emptyList()) { props.onUnfocus() }

  subnav(right = true) {
    subnavGroup {
      subnavItem(hoverable = false) {
        +"Signed in as"
        b { +name }
      }
    }
    if (auth.canVisit(OrgSettingsPage)) {
      subnavGroup {
        navLink<RProps>(to = OrgSettingsPage.path) { subnavItem { +OrgSettingsPage.name } }
      }
    }
    subnavGroup {
      navLink<RProps>(to = SignOutPage.path, exact = true) { subnavItem { +SignOutPage.name } }
    }
  }
}
