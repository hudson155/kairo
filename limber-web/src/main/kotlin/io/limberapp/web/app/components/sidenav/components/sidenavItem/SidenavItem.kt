package io.limberapp.web.app.components.sidenav.components.sidenavItem

import io.limberapp.web.util.Theme
import kotlinx.css.Align
import kotlinx.css.BorderStyle
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.alignItems
import kotlinx.css.backgroundColor
import kotlinx.css.borderBottomStyle
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.padding
import kotlinx.css.properties.borderBottom
import kotlinx.css.px
import react.RBuilder
import react.RHandler
import react.RProps
import react.child
import react.functionalComponent
import styled.css
import styled.styledDiv

/**
 * A single non-link item on a sidenav.
 */
internal fun RBuilder.sidenavItem(children: RHandler<RProps>) {
    child(sidenavItem, handler = children)
}

private val sidenavItem = functionalComponent<RProps> { props ->
    styledDiv {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = Align.flexStart
            backgroundColor = Theme.backgroundLightImportant
            padding(8.px)
            borderBottom(1.px, BorderStyle.solid, Theme.borderLight)
            lastOfType {
                borderBottomStyle = BorderStyle.none
            }
        }
        props.children()
    }
}
