package io.limberapp.web.app.components.navbar.components.headerPhoto

import io.limberapp.web.util.Styles
import kotlinx.css.height
import kotlinx.css.marginRight
import kotlinx.css.maxWidth
import kotlinx.css.px
import react.RBuilder
import react.RProps
import react.child
import react.dom.img
import react.functionalComponent
import styled.getClassName

/**
 * A photo on a top-of-page navbar. A common use case is for a profile photo.
 */
internal fun RBuilder.headerPhoto(url: String) {
    child(headerPhoto, Props(url))
}

internal data class Props(val url: String) : RProps

private val styles = object : Styles("HeaderPhoto") {
    val img by css {
        marginRight = 16.px
        height = 32.px
        maxWidth = 32.px
    }
}.apply { inject() }

private val headerPhoto = functionalComponent<Props> { props ->
    img(src = props.url, classes = styles.getClassName { it::img }) {}
}
