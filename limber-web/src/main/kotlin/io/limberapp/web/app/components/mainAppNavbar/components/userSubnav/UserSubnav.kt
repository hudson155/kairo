package io.limberapp.web.app.components.mainAppNavbar.components.userSubnav

import io.limberapp.web.app.components.navbar.components.subnav.components.subnavGroup.subnavGroup
import io.limberapp.web.app.components.navbar.components.subnav.components.subnavItem.subnavItem
import io.limberapp.web.app.components.navbar.components.subnav.subnav
import io.limberapp.web.context.globalState.useGlobalState
import react.RBuilder
import react.RProps
import react.child
import react.dom.b
import react.functionalComponent
import react.router.dom.navLink

/**
 * Subnav on the main app's top-of-page nav that shows up when the user's name/photo is clicked.
 */
internal fun RBuilder.userSubnav() {
    child(userSubnav)
}

private val userSubnav = functionalComponent<RProps> {
    val global = useGlobalState()

    val name = checkNotNull(global.state.user.state).fullName

    subnav {
        subnavGroup {
            subnavItem {
                +"Signed in as"
                b { +name }
            }
        }
        subnavGroup {
            navLink<RProps>(to = "/signout", exact = true) { subnavItem { +"Sign Out" } }
        }
    }
}
