package io.limberapp.web.app.components.layouts.standardLayoutWithLeftPane

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

internal fun RBuilder.standardLayoutWithLeftPane(title: String, leftPane: ReactElement?, children: RHandler<Props>) {
    child(standardLayoutWithLeftPane, Props(title, leftPane), handler = children)
}

internal data class Props(val title: String, val leftPane: ReactElement?) : RProps

private val standardLayoutWithLeftPane = functionalComponent<Props> { props ->
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
            styledDiv {
                css {
                    flexBasis = 256.px.basis
                    display = Display.flex
                    flexDirection = FlexDirection.column
                    marginRight = 16.px
                }
                props.leftPane?.let { child(it) }
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
