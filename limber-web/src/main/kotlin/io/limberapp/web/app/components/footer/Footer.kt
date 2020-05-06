package io.limberapp.web.app.components.footer

import io.limberapp.web.util.Styles
import io.limberapp.web.util.process
import kotlinx.css.Display
import kotlinx.css.display
import kotlinx.css.padding
import kotlinx.css.px
import react.RBuilder
import react.dom.div
import react.dom.p
import react.dom.small
import styled.getClassName
import kotlin.js.Date

/**
 * Bottom-of-page footer for use at the bottom of all pages.
 */
internal fun RBuilder.footer() {
    div(classes = styles.getClassName { it::container }) {
        p { small { +"Copyright © ${Date().getFullYear()} ${process.env.COPYRIGHT_HOLDER}" } }
    }
}

private val styles = object : Styles("Footer") {
    val container by css {
        display = Display.flex
        padding(vertical = 8.px, horizontal = 16.px)
    }
}.apply { inject() }
