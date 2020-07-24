package io.limberapp.web.app.components.mainAppNavbar

import io.limberapp.backend.module.orgs.rep.org.default
import io.limberapp.web.app.components.hamburgerableNavbar.hamburgerableNavbar
import io.limberapp.web.app.components.mainAppNavbar.components.userSubnav.userSubnav
import io.limberapp.web.app.components.navbar.components.headerGroup.headerGroup
import io.limberapp.web.app.components.navbar.components.headerItem.headerItem
import io.limberapp.web.app.components.profilePhoto.profilePhoto
import io.limberapp.web.state.state.org.useOrgState
import io.limberapp.web.state.state.user.useUserState
import io.limberapp.web.util.Styles
import io.limberapp.web.util.buildElements
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import io.limberapp.web.util.gs
import io.limberapp.web.util.initials
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*
import react.router.dom.*

/**
 * Top-of-page nav for use on most pages in the main app when in an authenticated state.
 */
internal fun RBuilder.mainAppNavbar() {
  child(component)
}

internal typealias Props = RProps

private class S : Styles("MainAppNavbar") {
  val right by css {
    display = Display.flex
    flexDirection = FlexDirection.row
    alignItems = Align.center
    cursor = Cursor.pointer
  }
  val openRight by css {
    cursor = Cursor.initial
  }
}

private val s = S().apply { inject() }

private enum class OpenItem { HAMBURGER, USER_DROPDOWN }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val (org, _) = useOrgState()
  val (user, _) = useUserState()

  // Only 1 item on the navbar can be open at a time.
  val (openItem, setOpenItem) = useState<OpenItem?>(null)

  val features = org.features
  val name = user.fullName
  val photoUrl = user.profilePhotoUrl

  hamburgerableNavbar(
    left = buildElements {
      headerGroup {
        features.default?.let { navLink<RProps>(to = it.path) { headerItem { +"Limber" } } }
      }
    },
    right = buildElements {
      headerGroup {
        a(classes = cls(s.c { it::right }, s.c(openItem == OpenItem.USER_DROPDOWN) { it::openRight })) {
          attrs.onClickFunction = { setOpenItem(OpenItem.USER_DROPDOWN) }
          headerItem(classes = gs.c { it::hiddenXs }) { +name }
          profilePhoto(placeholder = name.initials, url = photoUrl, small = true)
        }
        if (openItem == OpenItem.USER_DROPDOWN) {
          userSubnav(onUnfocus = { setOpenItem(null) })
        }
      }
    },
    features = features,
    hamburgerOpen = openItem == OpenItem.HAMBURGER,
    onHamburger = { open -> setOpenItem(if (open) OpenItem.HAMBURGER else null) }
  )
}
