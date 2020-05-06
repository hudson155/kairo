package io.limberapp.web.app.pages.loadingPage

import io.limberapp.web.app.components.inlineIcon.inlineIcon
import io.limberapp.web.app.components.layout.components.centeredContentLayout.centeredContentLayout
import io.limberapp.web.util.Styles
import io.limberapp.web.util.globalStyles
import kotlinx.css.fontSize
import kotlinx.css.px
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
    val spinnerContainer by css {
        fontSize = 48.px
    }
}.apply { inject() }

private val loadingPage = functionalComponent<Props> { props ->
    centeredContentLayout {
        div(classes = styles.getClassName { it::spinnerContainer }) {
            inlineIcon("spinner", classes = globalStyles.getClassName { it::spinner })
        }
        p { +props.loadingText }
    }
}
