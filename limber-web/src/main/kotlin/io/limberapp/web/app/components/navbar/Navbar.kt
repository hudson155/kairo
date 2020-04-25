package io.limberapp.web.app.components.navbar

import io.limberapp.web.context.theme.useTheme
import kotlinx.css.Display
import kotlinx.css.JustifyContent
import kotlinx.css.backgroundColor
import kotlinx.css.display
import kotlinx.css.height
import kotlinx.css.justifyContent
import kotlinx.css.padding
import kotlinx.css.px
import react.RBuilder
import react.RHandler
import react.RProps
import react.ReactElement
import react.child
import react.functionalComponent
import styled.css
import styled.styledDiv

internal data class Props(val left: ReactElement?, val right: ReactElement?) : RProps

private val navbar = functionalComponent<Props> { props ->
    val theme = useTheme()

    styledDiv {
        css {
            display = Display.flex
            justifyContent = JustifyContent.spaceBetween
            height = 32.px
            backgroundColor = theme.backgroundDark
            padding(vertical = 16.px, horizontal = 0.px)
        }
        styledDiv {
            css { display = Display.flex }
            props.left?.let { child(it) }
            props.children()
        }
        styledDiv {
            css { display = Display.flex }
            props.right?.let { child(it) }
        }
    }
}

internal fun RBuilder.navbar(left: ReactElement?, right: ReactElement?, children: RHandler<Props> = {}) {
    child(navbar, Props(left, right), handler = children)
}
