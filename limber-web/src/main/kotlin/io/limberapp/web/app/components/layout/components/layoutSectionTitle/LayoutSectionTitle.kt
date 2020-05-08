package io.limberapp.web.app.components.layout.components.layoutSectionTitle

import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import kotlinx.css.BorderStyle
import kotlinx.css.marginBottom
import kotlinx.css.marginTop
import kotlinx.css.properties.borderBottom
import kotlinx.css.px
import react.RBuilder
import react.RProps
import react.child
import react.dom.div
import react.dom.h2
import react.dom.p
import react.functionalComponent
import styled.getClassName

/**
 * The title at the top of a section, plus an optional description.
 */
internal fun RBuilder.layoutSectionTitle(title: String, description: String? = null) {
    child(component, Props(title, description))
}

internal data class Props(val title: String, val description: String?) : RProps

private val styles = object : Styles("LayoutSectionTitle") {
    val container by css {
        marginBottom = 24.px
        borderBottom(1.px, BorderStyle.solid, Theme.Color.borderLight)
    }
    val title by css {
        marginTop = 0.px
        marginBottom = 6.px
    }
    val description by css {
        marginTop = 0.px
        marginBottom = 6.px
    }
}.apply { inject() }

private val component = functionalComponent<Props> { props ->
    div(classes = styles.getClassName { it::container }) {
        h2(classes = styles.getClassName { it::title }) { +props.title }
        props.description?.let { p(classes = styles.getClassName { it::description }) { +it } }
    }
}
