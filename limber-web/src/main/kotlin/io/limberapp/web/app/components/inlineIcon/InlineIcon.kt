package io.limberapp.web.app.components.inlineIcon

import io.limberapp.web.util.Styles
import io.limberapp.web.util.cls
import io.limberapp.web.util.c
import kotlinx.css.marginLeft
import kotlinx.css.px
import react.*
import react.dom.*

/**
 * Icon in-line with text.
 */
internal fun RBuilder.inlineIcon(name: String, classes: String? = null) {
  // TODO: It would be nice to use a less rough version of these icons than "fas", but "fas" is the only free
  //  version.
  i(classes = cls(s.c { it::i }, "fas", "fa-$name", classes)) {}
}

private val s = object : Styles("InlineIcon") {
  val i by css {
    marginLeft = 6.px
  }
}.apply { inject() }
