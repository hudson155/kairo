package io.limberapp.web.app.components.navbar.components.subnav

import io.limberapp.web.context.theme.useTheme
import kotlinx.css.Align
import kotlinx.css.BorderStyle
import kotlinx.css.Color
import kotlinx.css.Display
import kotlinx.css.LinearDimension
import kotlinx.css.Position
import kotlinx.css.QuotedString
import kotlinx.css.alignSelf
import kotlinx.css.backgroundColor
import kotlinx.css.borderBottomColor
import kotlinx.css.borderRadius
import kotlinx.css.content
import kotlinx.css.display
import kotlinx.css.left
import kotlinx.css.marginRight
import kotlinx.css.position
import kotlinx.css.properties.border
import kotlinx.css.px
import kotlinx.css.right
import kotlinx.css.top
import kotlinx.css.width
import react.RBuilder
import react.RHandler
import react.RProps
import react.child
import react.functionalComponent
import styled.css
import styled.styledDiv

/**
 * Generic navigational component that drops down from a top-of-page navbar. It's generally only visible when a nav link
 * is active, but that functionality must be managed outside the scope of this component.
 */
internal fun RBuilder.subnav(children: RHandler<RProps>) {
    child(subnav, handler = children)
}

private val subnav = functionalComponent<RProps> { props ->
    val theme = useTheme()

    // TODO: In order for this to be truly reusable the positioning likely needs to be altered.
    styledDiv {
        css {
            val widthPx = 192 // The width of this component.
            val afterOffsetPx = 22 // How far in the caret ::after element is.
            val centeringWidth = 32 // Center the caret under a component of this width.
            alignSelf = Align.flexStart
            position = Position.relative
            top = 44.px
            right = (widthPx - afterOffsetPx / 2 + centeringWidth / 2).px
            width = widthPx.px
            marginRight = (-widthPx - 2 * 1).px
            backgroundColor = theme.backgroundLight
            border(1.px, BorderStyle.solid, theme.borderLight)
            borderRadius = 4.px
            after {
                top = (-14).px
                right = afterOffsetPx.px
                left = LinearDimension.auto
                border(7.px, BorderStyle.solid, Color.transparent)
                borderBottomColor = theme.backgroundLight
                position = Position.absolute
                display = Display.inlineBlock
                content = QuotedString("")
            }
        }
        props.children()
    }
}
