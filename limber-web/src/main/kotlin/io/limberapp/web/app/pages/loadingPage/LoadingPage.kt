package io.limberapp.web.app.pages.loadingPage

import io.limberapp.web.context.theme.useTheme
import kotlinx.css.Align
import kotlinx.css.BorderStyle
import kotlinx.css.BoxSizing
import kotlinx.css.Color
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.JustifyContent
import kotlinx.css.Position
import kotlinx.css.alignItems
import kotlinx.css.borderBottomColor
import kotlinx.css.borderLeftColor
import kotlinx.css.borderRadius
import kotlinx.css.borderRightColor
import kotlinx.css.borderTopColor
import kotlinx.css.boxSizing
import kotlinx.css.content
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.flexGrow
import kotlinx.css.height
import kotlinx.css.justifyContent
import kotlinx.css.margin
import kotlinx.css.pct
import kotlinx.css.position
import kotlinx.css.properties.IterationCount
import kotlinx.css.properties.animation
import kotlinx.css.properties.border
import kotlinx.css.properties.s
import kotlinx.css.px
import kotlinx.css.quoted
import kotlinx.css.width
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import styled.StyleSheet
import styled.css
import styled.styledDiv
import styled.styledP

/**
 * Page to show while things are loading.
 */
internal fun RBuilder.loadingPage(loadingText: String) {
    child(loadingPage, Props(loadingText))
}

internal data class Props(val loadingText: String) : RProps

private val loadingPage = functionalComponent<Props> { props ->

    val theme = useTheme()

    val styles = object : StyleSheet("LoadingPageStyles", isStatic = true) {

        val root by css {
            flexGrow = 1.0
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = Align.center
            justifyContent = JustifyContent.center
        }

        val spinner by css {
            display = Display.inlineBlock
            position = Position.relative
            width = 80.px
            height = 80.px
            after {
                content = "".quoted
                display = Display.block
                borderRadius = 50.pct
                width = 0.px
                height = 0.px
                margin(8.px)
                boxSizing = BoxSizing.borderBox
                border(width = 32.px, style = BorderStyle.solid, color = theme.backgroundDark)
                borderTopColor = theme.backgroundDark
                borderRightColor = Color.transparent
                borderBottomColor = theme.backgroundDark
                borderLeftColor = Color.transparent
                animation("loading-spinner", duration = 1.2.s, iterationCount = IterationCount.infinite)
            }
        }
    }

    styledDiv {
        css { +styles.root }
        styledDiv {
            css { +styles.spinner }
        }
        styledP { +props.loadingText }
    }
}
