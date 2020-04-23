package io.limberapp.web.app.components.navbar.components.headerLink

import kotlinx.css.Align
import kotlinx.css.Color
import kotlinx.css.Display
import kotlinx.css.FontWeight
import kotlinx.css.alignItems
import kotlinx.css.color
import kotlinx.css.display
import kotlinx.css.fontWeight
import kotlinx.css.marginRight
import kotlinx.css.px
import react.RBuilder
import react.RHandler
import react.RProps
import react.functionalComponent
import react.router.dom.navLink
import styled.css
import styled.styledDiv

internal data class Props(val to: String) : RProps

private val headerLink = functionalComponent<Props> { props ->
    styledDiv {
        css {
            display = Display.flex
            alignItems = Align.center
            marginRight = 16.px
            color = Color.white
            fontWeight = FontWeight.bold
        }
        navLink<RProps>(to = props.to, exact = true) {
            props.children()
        }
    }
}

internal fun RBuilder.headerLink(to: String, children: RHandler<Props>) {
    child(headerLink, Props(to), handler = children)
}
