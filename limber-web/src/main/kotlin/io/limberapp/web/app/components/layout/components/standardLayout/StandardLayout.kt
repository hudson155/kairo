package io.limberapp.web.app.components.layout.components.standardLayout

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
import react.functionalComponent
import styled.css
import styled.styledDiv

/**
 * The layout for most pages, supporting a vertical group of elements as the main body ([children]), and an optional
 * [leftPane] which is intended for side navigation.
 */
internal fun RBuilder.standardLayout(leftPane: ReactElement? = null, children: RHandler<Props>) {
    child(standardLayout, Props(leftPane), handler = children)
}

internal data class Props(val leftPane: ReactElement?) : RProps

private val standardLayout = functionalComponent<Props> { props ->
    styledDiv {
        css {
            flexGrow = 1.0
            display = Display.flex
            flexDirection = FlexDirection.row
            justifyContent = JustifyContent.center
            margin(8.px)
            paddingTop = 32.px
        }
        styledDiv {
            css {
                flexBasis = 1200.px.basis
                display = Display.flex
                flexDirection = FlexDirection.row
            }
            props.leftPane?.let {
                styledDiv {
                    css {
                        flexBasis = 256.px.basis
                        display = Display.flex
                        flexDirection = FlexDirection.column
                        marginRight = 16.px
                    }
                    child(it)
                }
            }
            styledDiv {
                css {
                    flexGrow = 1.0
                    display = Display.flex
                    flexDirection = FlexDirection.column
                }
                props.children()
            }
        }
    }
}
