package io.limberapp.web.app.components.tabbedView.components.tabbedViewLink

import com.piperframework.util.replaceLastPathComponentWith
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import kotlinx.css.*
import kotlinx.css.properties.*
import react.*
import react.dom.*
import react.router.dom.*

internal fun RBuilder.tabbedViewLink(name: String, subpath: String) {
  child(component, Props(name, subpath))
}

internal data class Props(val name: String, val subpath: String) : RProps

private val s = object : Styles("TabbedViewLink") {
  val navLink by css {
    marginRight = 12.px
    borderBottom(2.px, BorderStyle.solid, Theme.Color.Border.dark)
    lastOfType {
      marginRight = 0.px
    }
    hover {
      color = Theme.Color.Text.link
    }
  }
  val activeNavLink by css {
    borderBottomColor = Theme.Color.smallActiveIndicator
  }
}.apply { inject() }

private val component = functionalComponent<Props> { props ->
  val match = checkNotNull(useRouteMatch<RProps>())

  navLink<RProps>(
    to = match.url.replaceLastPathComponentWith(props.subpath),
    replace = true,
    exact = true,
    className = s.c { it::navLink },
    activeClassName = s.c { it::activeNavLink }
  ) {
    b { +props.name }
  }
}
