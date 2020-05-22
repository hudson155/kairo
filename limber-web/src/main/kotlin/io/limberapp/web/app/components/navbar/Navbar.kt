package io.limberapp.web.app.components.navbar

import io.limberapp.web.app.components.navbar.components.headerGroup.headerGroup
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import io.limberapp.web.util.gs
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
 * slightly to the right of the [left] section.
 *
 * Typically, [left] would be used for branding. It shows up on the far left side of the navbar. It does not show up at
 * all on small screen sizes.
 *
 * The [right] shows up on the far right side of the navbar. It always shows up, even on small screen sizes.
 *
 * Typically, [children] would be used for actual navigation links. On large screen sizes, it shows up just to the right
 * of [left]. It should be a series of [headerGroup]s. It does not show up by default on small screen sizes, and is
 * instead accessible in a hamburger menu.
 */
internal fun RBuilder.navbar(left: ReactElement?, right: ReactElement?, children: RHandler<Props>) {
  child(component, Props(left, right), handler = children)
}

internal data class Props(val left: ReactElement?, val right: ReactElement?) : RProps

private class S : Styles("Navbar") {
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
  val subsection by css {
    display = Display.flex
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent<Props> { props ->
  div(classes = s.c { it::container }) {
    div(classes = s.c { it::section }) {
      div(classes = s.c { it::subsection }) {
        props.left?.let { child(it) }
      }
      div(classes = cls(s.c { it::subsection }, gs.c { it::hiddenXs })) {
        props.children()
      }
    }
    div(classes = s.c { it::section }) {
      props.right?.let { child(it) }
    }
  }
}
