package io.limberapp.web.app.components.navbar

import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import kotlinx.css.Display
import kotlinx.css.JustifyContent
import kotlinx.css.backgroundColor
import kotlinx.css.display
import kotlinx.css.height
import kotlinx.css.justifyContent
import kotlinx.css.padding
import kotlinx.css.px
import react.*
import react.dom.div

/**
 * Generic top-of-page navbar that supports a [left] section, [right] section, and [children] as a left-aligned section
 * slightly to the right of the [left] section. Typically, [left] would be used for branding, and [children] would be
 * used for actual navigation links.
 */
internal fun RBuilder.navbar(left: ReactElement?, right: ReactElement?, children: RHandler<Props>) {
  child(component, Props(left, right), handler = children)
}

internal data class Props(val left: ReactElement?, val right: ReactElement?) : RProps

private val s = object : Styles("Navbar") {
  val container by css {
    display = Display.flex
    justifyContent = JustifyContent.spaceBetween
    height = 32.px
    backgroundColor = Theme.Color.Background.dark
    padding(vertical = 16.px, horizontal = 0.px)
  }
  val section by css {
    display = Display.flex
  }
}.apply { inject() }

private val component = functionalComponent<Props> { props ->
  div(classes = s.c { it::container }) {
    div(classes = s.c { it::section }) {
      props.left?.let { child(it) }
      props.children()
    }
    div(classes = s.c { it::section }) {
      props.right?.let { child(it) }
    }
  }
}
