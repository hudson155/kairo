package io.limberapp.web.app.components.navbar.components.headerPhoto

import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import kotlinx.css.borderRadius
import kotlinx.css.height
import kotlinx.css.marginRight
import kotlinx.css.maxWidth
import kotlinx.css.px
import react.*
import react.dom.*
import styled.getClassName

/**
 * A photo on a top-of-page navbar. A common use case is for a profile photo.
 */
internal fun RBuilder.headerPhoto(url: String) {
  img(src = url, classes = styles.getClassName { it::img }) {}
}

private val styles = object : Styles("HeaderPhoto") {
  val img by css {
    marginRight = 16.px
    height = 32.px
    maxWidth = 32.px
    borderRadius = Theme.Sizing.borderRadius
  }
}.apply { inject() }
