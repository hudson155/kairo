package io.limberapp.web.app.components.sidenav

import io.limberapp.web.app.components.sidenav.components.sidenavGroup.sidenavGroup
import io.limberapp.web.app.components.sidenav.components.sidenavItem.sidenavItem
import io.limberapp.web.util.Styles
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.display
import kotlinx.css.flexDirection
import react.RBuilder
import react.RHandler
import react.RProps
import react.dom.b
import react.dom.div
import react.functionalComponent
import styled.getClassName

/**
 * Sidenav for navigation within a feature.
 */
internal fun RBuilder.sidenav(title: String, children: RHandler<RProps>) {
    child(component, Props(title), handler = children)
}

internal data class Props(val title: String) : RProps

private val styles = object : Styles("Sidenav") {
    val container by css {
        display = Display.flex
        flexDirection = FlexDirection.column
    }
}.apply { inject() }

private val component = functionalComponent<Props> { props ->
    div(classes = styles.getClassName { it::container }) {
        sidenavGroup { sidenavItem { b { +props.title } } }
        props.children()
    }
}
