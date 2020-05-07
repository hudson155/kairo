package io.limberapp.web.app.components.sidenav.components.sidenavGroup

import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import kotlinx.css.BorderStyle
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.Overflow
import kotlinx.css.borderRadius
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.marginBottom
import kotlinx.css.overflow
import kotlinx.css.properties.border
import kotlinx.css.px
import react.RBuilder
import react.RHandler
import react.RProps
import react.child
import react.dom.div
import react.functionalComponent
import styled.getClassName

/**
 * A group of items on a sidenav. Items in the same group should be conceptually grouped. The physical spacing between
 * them will be less for items in the same group than for items in different groups.
 */
internal fun RBuilder.sidenavGroup(children: RHandler<RProps>) {
    child(sidenavGroup, handler = children)
}

private val styles = object : Styles("SidenavGroup") {
    val container by css {
        display = Display.flex
        flexDirection = FlexDirection.column
        border(1.px, BorderStyle.solid, Theme.Color.borderLight)
        borderRadius = Theme.Sizing.borderRadius
        marginBottom = 16.px
        overflow = Overflow.hidden // Avoid background color overflow.
        lastOfType {
            marginBottom = 0.px
        }
    }
}.apply { inject() }

private val sidenavGroup = functionalComponent<RProps> { props ->
    div(classes = styles.getClassName { it::container }) {
        props.children()
    }
}
