package io.limberapp.web.app.components.sidenav

import io.limberapp.web.app.components.sidenav.components.sidenavGroup.sidenavGroup
import io.limberapp.web.app.components.sidenav.components.sidenavHeader.sidenavHeader
import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import io.limberapp.web.util.gs
import kotlinx.css.*
import react.*
import react.dom.*
import react.router.dom.*

/**
 * Sidenav for navigation within a feature. Sidenavs are not collapsable on large screen sizes, but are collapsable on
 * small screen sizes.
 *
 * The [title] is shown in the header above everything else, and manages collapsibility.
 *
 * [children] should be a series of [sidenavGroup]s.
 */
internal fun RBuilder.sidenav(title: String, children: RHandler<Props>) {
  child(component, Props(title), handler = children)
}

internal data class Props(val title: String) : RProps

private class S : Styles("Sidenav") {
  val container by css {
    display = Display.flex
    flexDirection = FlexDirection.column
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val location = useLocation()

  val (isOpen, setIsOpen) = useState(false)

  // Closes the dropdown when the path changes.
  useEffect(listOf(location.pathname)) {
    setIsOpen(false)
  }

  div(classes = s.c { it::container }) {
    sidenavHeader(props.title, isOpen = isOpen, onClick = { setIsOpen(!isOpen) })
    div(classes = gs.c(!isOpen) { it::hiddenXs }) { props.children() }
  }
}
