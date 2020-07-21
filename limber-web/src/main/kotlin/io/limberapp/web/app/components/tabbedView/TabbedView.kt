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

/**
 * A series of [tabbedViewLink]s that represent pages within some component. Each [tabbedViewLink] is its own page.
 * Clicking on one of them will replace the last component of the URL with the subpath for that [tabbedViewLink].
 *
 * The [tabNames] are the of items to show. Subpaths are automatically generated from these names.
 */
internal fun RBuilder.tabbedView(vararg tabNames: String) {
  child(component, Props(tabNames.toList()))
}

internal data class Props(val tabNames: List<String>) : RProps

private class S : Styles("TabbedView") {
  val tabsSection by css {
    display = Display.flex
    flexDirection = FlexDirection.row
    justifyContent = JustifyContent.center
    paddingBottom = 8.px
    borderBottom(1.px, BorderStyle.solid, Theme.Color.Border.light)
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  div(classes = s.c { it::tabsSection }) {
    props.tabNames.forEach {
      tabbedViewLink(it, it.slugify())
    }
  }
}
