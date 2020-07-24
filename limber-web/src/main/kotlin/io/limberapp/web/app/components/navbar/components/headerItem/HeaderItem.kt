package io.limberapp.web.app.components.navbar.components.headerItem

import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import kotlinx.css.*
import react.*
import react.dom.*

internal fun RBuilder.headerItem(classes: String? = null, children: RHandler<Props>) {
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

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  div(classes = cls(s.c { it::container }, props.classes)) {
    b { props.children() }
  }
}
