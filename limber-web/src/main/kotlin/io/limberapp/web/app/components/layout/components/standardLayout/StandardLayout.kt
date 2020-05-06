package io.limberapp.web.app.components.layout.components.standardLayout

import io.limberapp.web.util.Styles
import io.limberapp.web.util.injectStyles
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.JustifyContent
import kotlinx.css.basis
import kotlinx.css.display
import kotlinx.css.flexBasis
import kotlinx.css.flexDirection
import kotlinx.css.flexGrow
import kotlinx.css.justifyContent
import kotlinx.css.margin
import kotlinx.css.marginRight
import kotlinx.css.paddingTop
import kotlinx.css.px
import react.RBuilder
import react.RHandler
import react.RProps
import react.ReactElement
import react.dom.div
import react.functionalComponent
import styled.getClassName

/**
 * The layout for most pages, supporting a vertical group of elements as the main body ([children]), and an optional
 * [leftPane] which is intended for side navigation.
 */
internal fun RBuilder.standardLayout(leftPane: ReactElement? = null, children: RHandler<Props>) {
    child(standardLayout, Props(leftPane), handler = children)
}

internal data class Props(val leftPane: ReactElement?) : RProps

private val styles = object : Styles("StandardLayout") {
    val outerContainer by css {
        flexGrow = 1.0
        display = Display.flex
        flexDirection = FlexDirection.row
        justifyContent = JustifyContent.center
        margin(16.px)
        paddingTop = 32.px
    }
    val innerContainer by css {
        flexBasis = 1200.px.basis
        display = Display.flex
        flexDirection = FlexDirection.row
    }
    val leftPane by css {
        flexBasis = 256.px.basis
        display = Display.flex
        flexDirection = FlexDirection.column
        marginRight = 48.px
    }
    val mainContent by css {
        flexGrow = 1.0
        display = Display.flex
        flexDirection = FlexDirection.column
    }
}

private val standardLayout = functionalComponent<Props> { props ->
    injectStyles(styles)

    div(classes = styles.getClassName { it::outerContainer }) {
        div(classes = styles.getClassName { it::innerContainer }) {
            props.leftPane?.let {
                div(classes = styles.getClassName { it::leftPane }) {
                    child(it)
                }
            }
            div(classes = styles.getClassName { it::mainContent }) {
                props.children()
            }
        }
    }
}
