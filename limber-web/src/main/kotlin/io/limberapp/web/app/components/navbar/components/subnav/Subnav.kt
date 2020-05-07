package io.limberapp.web.app.components.navbar.components.subnav

import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
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
import org.w3c.dom.Element
import react.RBuilder
import react.RHandler
import react.RMutableRef
import react.RProps
import react.dom.div
import react.functionalComponent
import styled.getClassName

/**
 * Generic navigational component that drops down from a top-of-page navbar. It's generally only visible when a nav link
 * is active, but that functionality must be managed outside the scope of this component. The [node] is a [RMutableRef]
 * that will be assigned to the subnav div, allowing the parent component to detect clicks outside of it.
 */
internal fun RBuilder.subnav(node: RMutableRef<Element?>, children: RHandler<RProps>) {
    child(subnav, Props(node), handler = children)
}

internal data class Props(val node: RMutableRef<Element?>) : RProps

// TODO: In order for this to be truly reusable the positioning likely needs to be altered.
private val styles = object : Styles("Subnav") {
    val container by css {
        val widthPx = 192 // The width of this component.
        val afterOffsetPx = 22 // How far in the caret ::after element is.
        val centeringWidth = 32 // Center the caret under a component of this width.
        alignSelf = Align.flexStart
        position = Position.relative
        top = 44.px
        right = (widthPx - afterOffsetPx / 2 + centeringWidth / 2).px
        width = widthPx.px
        marginRight = (-widthPx - 2 * 1).px
        backgroundColor = Theme.Color.backgroundLight
        border(1.px, BorderStyle.solid, Theme.Color.borderLight)
        borderRadius = Theme.Sizing.borderRadius
        after {
            top = (-14).px
            right = afterOffsetPx.px
            left = LinearDimension.auto
            border(7.px, BorderStyle.solid, Color.transparent)
            borderBottomColor = Theme.Color.backgroundLight
            position = Position.absolute
            display = Display.inlineBlock
            content = QuotedString("")
        }
    }
}.apply { inject() }

private val subnav = functionalComponent<Props> { props ->
    div(classes = styles.getClassName { it::container }) {
        ref = props.node
        props.children()
    }
}
