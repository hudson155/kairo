package io.limberapp.web.app.components.inlineIcon

import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import kotlinx.css.marginLeft
import kotlinx.css.px
import react.*
import react.dom.*

/**
 * Icon in-line with text. Uses the free version of Font Awesome as the icon library.
 *
 * The [name] is any icon name (excluding the leading "fa-") https://fontawesome.com/icons?d=gallery&m=free.
 *
 * [classes] are applied directly to the icon tag.
 *
 * Note that this isn't a component, it's actually just an extension function.
 */
internal fun RBuilder.inlineIcon(name: String, classes: String? = null) {
  // TODO: It would be nice to use a less rough version of these icons than "fas", but "fas" is the only free version.
  i(classes = cls(s.c { it::i }, "fas", "fa-$name", classes)) {}
}

private class S : Styles("InlineIcon") {
  val i by css {
    marginLeft = 6.px
  }
}

private val s = S().apply { inject() }
