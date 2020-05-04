package io.limberapp.web.app.components.mainAppNavbar

import io.limberapp.backend.module.orgs.rep.org.default
import io.limberapp.web.app.components.mainAppNavbar.components.userSubnav.userSubnav
import io.limberapp.web.app.components.navbar.components.headerGroup.headerGroup
import io.limberapp.web.app.components.navbar.components.headerItem.headerItem
import io.limberapp.web.app.components.navbar.components.headerPhoto.headerPhoto
import io.limberapp.web.app.components.navbar.navbar
import io.limberapp.web.context.globalState.useGlobalState
import io.limberapp.web.util.buildElements
import kotlinx.css.Align
import kotlinx.css.Cursor
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.alignItems
import kotlinx.css.cursor
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import react.router.dom.navLink
import react.useState
import styled.css
import styled.styledDiv

/**
 * Top-of-page nav for use on most pages in the main app when in an authenticated state.
 */
internal fun RBuilder.mainAppNavbar() {
    child(mainAppNavbar)
}

private enum class OpenItem { USER_DROPDOWN }

private val mainAppNavbar = functionalComponent<RProps> {
    val global = useGlobalState()

    // Only 1 item on the navbar can be open at a time.
    val (openItem, setOpenItem) = useState<OpenItem?>(null)

    val (name, photoUrl) = checkNotNull(global.state.user.state).let { Pair(it.fullName, it.profilePhotoUrl) }
    val features = checkNotNull(global.state.org.state).features

    navbar(
        left = buildElements {
            headerGroup { features.default?.let { navLink<RProps>(to = it.path) { headerItem { +"Limber" } } } }
        },
        right = buildElements {
            headerGroup {
                styledDiv { // This should semantically be a styledA but it breaks the app for some reason.
                    css {
                        display = Display.flex
                        flexDirection = FlexDirection.row
                        alignItems = Align.center
                        if (openItem != OpenItem.USER_DROPDOWN) cursor = Cursor.pointer
                    }
                    attrs.onClickFunction = { setOpenItem(OpenItem.USER_DROPDOWN) }
                    headerItem { +name }
                    photoUrl?.let { headerPhoto(it) }
                }
                if (openItem == OpenItem.USER_DROPDOWN) {
                    userSubnav(onUnfocus = { setOpenItem(null) })
                }
            }
        }
    ) {
        headerGroup {
            features.forEach {
                navLink<RProps>(to = it.path) { headerItem { +it.name } }
            }
        }
    }
}
