package io.limberapp.web.app.components.inlineIcon

import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import kotlinx.css.*
import react.*
import react.dom.*

/**
 * Icon in-line with text. Uses the free version of Font Awesome as the icon library.
 *
 * The [name] is any icon name (excluding the leading "fa-") https://fontawesome.com/icons?d=gallery&m=free.
 */
internal fun RBuilder.inlineIcon(
  name: String,
  leftMargin: Boolean = false,
  rightMargin: Boolean = false,
  classes: String? = null,
) {
  // TODO: It would be nice to use a less rough version of these icons than "fas", but "fas" is the only free version.
  i(
    classes = cls(
      s.c(leftMargin) { it::leftMargin },
      s.c(rightMargin) { it::rightMargin },
      "fas",
      "fa-$name",
      classes
    )
  ) {}
}

private class S : Styles("InlineIcon") {
  val leftMargin by css {
    marginLeft = 6.px
  }
  val rightMargin by css {
    marginRight = 6.px
  }
}

private val s = S().apply { inject() }
