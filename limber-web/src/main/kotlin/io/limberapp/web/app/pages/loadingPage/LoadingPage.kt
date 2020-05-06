package io.limberapp.web.app.pages.loadingPage

import io.limberapp.web.app.components.layout.components.centeredContentLayout.centeredContentLayout
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import kotlinx.css.BorderStyle
import kotlinx.css.BoxSizing
import kotlinx.css.Color
import kotlinx.css.Display
import kotlinx.css.Position
import kotlinx.css.borderBottomColor
import kotlinx.css.borderLeftColor
import kotlinx.css.borderRadius
import kotlinx.css.borderRightColor
import kotlinx.css.borderTopColor
import kotlinx.css.boxSizing
import kotlinx.css.content
import kotlinx.css.display
import kotlinx.css.height
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
import react.dom.div
import react.dom.p
import react.functionalComponent
import styled.getClassName

/**
 * Page to show while things are loading.
 */
internal fun RBuilder.loadingPage(loadingText: String) {
    child(loadingPage, Props(loadingText))
}

internal data class Props(val loadingText: String) : RProps

private val styles = object : Styles("LoadingPage") {
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
            border(width = 32.px, style = BorderStyle.solid, color = Theme.backgroundDark)
            borderTopColor = Theme.backgroundDark
            borderRightColor = Color.transparent
            borderBottomColor = Theme.backgroundDark
            borderLeftColor = Color.transparent
            animation("loading-spinner", duration = 1.2.s, iterationCount = IterationCount.infinite)
        }
    }
}.apply { inject() }

private val loadingPage = functionalComponent<Props> { props ->
    centeredContentLayout {
        div(classes = styles.getClassName { it::spinner }) {}
        p { +props.loadingText }
    }
}
