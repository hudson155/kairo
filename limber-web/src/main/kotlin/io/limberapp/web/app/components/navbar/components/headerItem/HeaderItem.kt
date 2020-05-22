package io.limberapp.web.app.components.navbar.components.headerItem

import io.limberapp.web.app.components.navbar.components.headerGroup.headerGroup
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import kotlinx.css.*
import react.*
import react.dom.*

/**
 * A single item on a top-of-page navbar. This goes inside a [headerGroup].
 *
 * [children] should just be some text.
 */
internal fun RBuilder.headerItem(classes: String? = null, children: RHandler<RProps>) {
  child(component, Props(classes), handler = children)
}

internal data class Props(val classes: String?) : RProps

private class S : Styles("HeaderItem") {
  val container by css {
    display = Display.flex
    alignItems = Align.center
    marginRight = 16.px
    color = Theme.Color.Text.light
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent<Props> { props ->
  div(classes = cls(s.c { it::container }, props.classes)) {
    b { props.children() }
  }
}
