package io.limberapp.web.app.components.sidenav

import io.limberapp.web.app.components.sidenav.components.sidenavGroup.sidenavGroup
import io.limberapp.web.app.components.sidenav.components.sidenavItem.sidenavItem
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.display
import kotlinx.css.flexDirection
import react.RBuilder
import react.RHandler
import react.RProps
import react.dom.b
import react.functionalComponent
import styled.css
import styled.styledDiv

/**
 * Sidenav for navigation within a feature.
 */
internal fun RBuilder.sidenav(title: String, children: RHandler<RProps>) {
    child(sidenav, Props(title), handler = children)
}

internal data class Props(val title: String) : RProps

private val sidenav = functionalComponent<Props> { props ->
    styledDiv {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
        }
        sidenavGroup { sidenavItem { b { +props.title } } }
        props.children()
    }
}
