package io.limberapp.web.app.components.layout.components.standardLayout

import io.limberapp.web.util.Styles
import io.limberapp.web.util.c
import io.limberapp.web.util.notXs
import io.limberapp.web.util.xs
import kotlinx.css.*
import react.*
import react.dom.*

/**
 * The layout for most pages.
 *
 * The [leftPane] is intended for side navigation, and is optional.
 *
 * The [children] is a vertical group of elements as the main body.
 */
internal fun RBuilder.standardLayout(leftPane: ReactElement? = null, children: RHandler<Props>) {
  child(component, Props(leftPane), handler = children)
}

internal data class Props(val leftPane: ReactElement?) : RProps

private class S : Styles("StandardLayout") {
  val outerContainer by css {
    flexGrow = 1.0
    display = Display.flex
    flexDirection = FlexDirection.row
    justifyContent = JustifyContent.center
    margin(16.px)
    notXs {
      paddingTop = 32.px
    }
  }
  val innerContainer by css {
    flexBasis = 1200.px.basis
    display = Display.flex
    flexDirection = FlexDirection.row
    xs {
      flexDirection = FlexDirection.column
    }
  }
  val leftPane by css {
    display = Display.flex
    flexDirection = FlexDirection.column
    notXs {
      flexBasis = 256.px.basis
      marginRight = 48.px
    }
    xs {
      marginBottom = 16.px
    }
  }
  val mainContent by css {
    flexGrow = 1.0
    display = Display.flex
    flexDirection = FlexDirection.column
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  div(classes = s.c { it::outerContainer }) {
    div(classes = s.c { it::innerContainer }) {
      props.leftPane?.let {
        div(classes = s.c { it::leftPane }) {
          child(it)
        }
      }
      div(classes = s.c { it::mainContent }) {
        props.children()
      }
    }
  }
}
