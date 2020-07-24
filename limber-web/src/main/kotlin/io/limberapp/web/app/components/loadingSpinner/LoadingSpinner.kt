package io.limberapp.web.app.components.loadingSpinner

import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import kotlinx.css.*
import kotlinx.css.properties.*
import react.*
import react.dom.*

/**
 * Spinner to show while something is loading. Automatically centers itself horizontally.
 */
internal fun RBuilder.loadingSpinner(large: Boolean = false, classes: String? = null) {
  child(component, Props(large, classes))
}

internal data class Props(
  val large: Boolean,
  val classes: String?
) : RProps

/* Inspiration https://loading.io/spinner/eclipse/-eclipse-ring-circle-rotate */
private class S : Styles("LoadingSpinner") {
  val root by css {
    textAlign = TextAlign.center
    padding(12.px)
  }
  val container by css {
    display = Display.inlineBlock
    height = 16.px
    overflow = Overflow.hidden
    padding(2.px)
    width = 16.px
  }
  val largeContainer by css {
    height = 200.px
    width = 200.px
    padding(5.px)
  }
  val innerContainer by css {
    animation("spinner", 1.s, Timing.linear, iterationCount = IterationCount.infinite)
    height = 100.pct
    position = Position.relative
    transform {
      translateZ(0.px)
      scale(1)
    }
    width = 100.pct
  }
  val spinner by css {
    borderRadius = 50.pct
    boxShadow(Color.currentColor, 0.px, 2.px, 0.px, 0.px)
    boxSizing = BoxSizing.contentBox
    height = 100.pct
    position = Position.absolute
    width = 100.pct
  }
  val largeSpinner by css {
    boxShadow(Color.currentColor, 0.px, 4.px, 0.px, 0.px)
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  div(classes = cls(s.c { it::root }, props.classes)) {
    div(classes = cls(s.c { it::container }, s.c(props.large) { it::largeContainer })) {
      div(classes = s.c { it::innerContainer }) {
        div(classes = cls(s.c { it::spinner }, s.c(props.large) { it::largeSpinner })) {}
      }
    }
  }
}


