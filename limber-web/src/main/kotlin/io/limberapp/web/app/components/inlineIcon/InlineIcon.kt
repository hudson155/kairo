package io.limberapp.web.app.components.inlineIcon

import io.limberapp.web.util.Styles
import kotlinx.css.marginLeft
import kotlinx.css.px
import react.RBuilder
import react.dom.i
import styled.getClassName

private val styles = object : Styles("InlineIcon") {
    val i by css {
        marginLeft = 6.px
    }
}.apply { inject() }

internal fun RBuilder.inlineIcon(name: String, classes: String? = null) {
    // TODO: It would be nice to use a less rough version of these icons than "fas", but "fas" is the only free version.
    i(classes = listOfNotNull(styles.getClassName { it::i }, "fas", "fa-$name", classes).joinToString(" ")) {}
}
