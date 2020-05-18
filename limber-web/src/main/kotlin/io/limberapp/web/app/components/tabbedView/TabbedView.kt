package io.limberapp.web.app.components.tabbedView

import com.piperframework.util.slugify
import io.limberapp.web.app.components.tabbedView.components.tabbedViewLink.tabbedViewLink
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import kotlinx.css.*
import kotlinx.css.properties.*
import react.*
import react.dom.*

internal fun RBuilder.tabbedView(vararg tabNames: String) {
  child(component, Props(tabNames.toList()))
}

internal data class Props(val tabNames: List<String>) : RProps

private val s = object : Styles("TabbedView") {
  val tabsSection by css {
    display = Display.flex
    flexDirection = FlexDirection.row
    justifyContent = JustifyContent.center
    paddingBottom = 8.px
    borderBottom(1.px, BorderStyle.solid, Theme.Color.Border.light)
  }
}.apply { inject() }

private val component = functionalComponent<Props> { props ->
  div(classes = s.c { it::tabsSection }) {
    props.tabNames.forEach {
      tabbedViewLink(it, it.slugify())
    }
  }
}
