package io.limberapp.web.app.components.footer

import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import io.limberapp.web.util.process
import kotlinx.css.*
import react.*
import react.dom.*
import kotlin.js.Date

internal fun RBuilder.footer() {
  child(component)
}

internal typealias Props = RProps

private class S : Styles("Footer") {
  val container by css {
    display = Display.flex
    padding(vertical = 8.px, horizontal = 16.px)
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  div(classes = s.c { it::container }) {
    p { small { +"Copyright © ${Date().getFullYear()} ${process.env.COPYRIGHT_HOLDER}" } }
  }
}
