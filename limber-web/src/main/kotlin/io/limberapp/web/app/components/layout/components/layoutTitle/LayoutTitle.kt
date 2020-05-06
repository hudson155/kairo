package io.limberapp.web.app.components.layout.components.layoutTitle

import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.injectStyles
import kotlinx.css.BorderStyle
import kotlinx.css.marginBottom
import kotlinx.css.marginTop
import kotlinx.css.properties.borderBottom
import kotlinx.css.px
import react.RBuilder
import react.RProps
import react.child
import react.dom.div
import react.dom.h1
import react.dom.p
import react.functionalComponent
import styled.getClassName

/**
 * The title at the top of a layout, plus an optional description. All pages should have one of these.
 */
internal fun RBuilder.layoutTitle(title: String, description: String? = null) {
    child(layoutTitle, Props(title, description))
}

internal data class Props(val title: String, val description: String?) : RProps

private val styles = object : Styles("LayoutTitle") {
    val container by css {
        marginBottom = 48.px
        borderBottom(1.px, BorderStyle.solid, Theme.borderLight)
    }
    val title by css {
        marginTop = 0.px
        marginBottom = 8.px
    }
    val subtitle by css {
        marginTop = 0.px
        marginBottom = 8.px
    }
}

private val layoutTitle = functionalComponent<Props> { props ->
    injectStyles(styles)

    div(classes = styles.getClassName { it::container }) {
        h1(classes = styles.getClassName { it::title }) { +props.title }
        props.description?.let { p(classes = styles.getClassName { it::subtitle }) { +it } }
    }
}
