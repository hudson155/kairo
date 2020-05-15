package io.limberapp.web.app.components.mainAppNavbar.components.userSubnav

import io.limberapp.web.app.components.navbar.components.subnav.components.subnavGroup.subnavGroup
import io.limberapp.web.app.components.navbar.components.subnav.components.subnavItem.subnavItem
import io.limberapp.web.app.components.navbar.components.subnav.subnav
import io.limberapp.web.app.pages.orgSettingsPage.OrgSettingsPage
import io.limberapp.web.app.pages.signOutPage.SignOutPage
import io.limberapp.web.context.globalState.useGlobalState
import io.limberapp.web.util.EventType
import org.w3c.dom.Element
import org.w3c.dom.events.Event
import react.RBuilder
import react.RProps
import react.child
import react.dom.b
import react.functionalComponent
import react.router.dom.navLink
import react.useEffectWithCleanup
import react.useRef
import kotlin.browser.document

/**
 * Subnav on the main app's top-of-page nav that shows up when the user's name/photo is clicked. [onUnfocus] is the
 * function to call when there's a click outside of this element. Normally, calling this function should hide the
 * subnav.
 */
internal fun RBuilder.userSubnav(onUnfocus: () -> Unit) {
  child(component, Props(onUnfocus))
}

internal data class Props(val onUnfocus: () -> Unit) : RProps

private val component = functionalComponent<Props> { props ->
  val global = useGlobalState()
  val node = useRef<Element?>(null)

  val name = checkNotNull(global.state.user.state).fullName

  val handleClick = { _: Event -> props.onUnfocus() }
  useEffectWithCleanup(emptyList()) {
    document.addEventListener(EventType.click, handleClick)
    return@useEffectWithCleanup { document.removeEventListener(EventType.click, handleClick) }
  }

  subnav(node) {
    subnavGroup {
      subnavItem(hoverable = false) {
        +"Signed in as"
        b { +name }
      }
    }
    subnavGroup {
      navLink<RProps>(to = OrgSettingsPage.path) { subnavItem { +OrgSettingsPage.name } }
    }
    subnavGroup {
      navLink<RProps>(to = SignOutPage.path, exact = true) { subnavItem { +SignOutPage.name } }
    }
  }
}
