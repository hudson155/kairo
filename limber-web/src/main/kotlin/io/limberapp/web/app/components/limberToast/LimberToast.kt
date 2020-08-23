package io.limberapp.web.app.components.limberToast

import io.limberapp.web.app.components.inlineIcon.inlineIcon
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import io.limberapp.web.util.xs
import kotlinx.css.*
import kotlinx.css.properties.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*

internal fun RBuilder.limberToast(
  message: String,
  onClose: () -> Unit,
  open: Boolean,
  status: LimberToastStatus,
) {
  child(component, Props(message, onClose, open, status))
}

private data class Props(
  val message: String,
  val onClose: () -> Unit,
  val open: Boolean,
  val status: LimberToastStatus,
) : RProps

internal enum class LimberToastStatus(val icon: String, val label: String, val styling: String) {
  ERROR("exclamation-circle", "Error", s.c { it::error }),
  SUCCESS("check-circle", "Success", s.c { it::success }),
  WARNING("exclamation-triangle", "Warning", s.c { it::warning }),
}

private class S : Styles("LimberToast") {
  val root by css {
    display = Display.flex
    flexDirection = FlexDirection.row
    alignItems = Align.center
    position = Position.absolute
    bottom = 10.px
    right = 10.px
    minHeight = 50.px
    minWidth = 350.px
    backgroundColor = Theme.Color.Background.white
    borderRadius = 3.px
    boxShadow(color = Theme.Color.Border.light, blurRadius = 14.px, spreadRadius = (-1).px)
    padding(10.px)
    transition("transform", 0.15.s, Timing.easeInOut)

    xs {
      left = 10.px
      minWidth = 0.px
    }
  }
  val open by css {
    transform.translate3d(0.0.pct, 0.0.pct, 0.0.pct)
  }
  val closed by css {
    transform.translate3d(110.pct, 0.0.pct, 0.0.pct)
  }
  val content by css {
    display = Display.flex
    flexDirection = FlexDirection.column
    color = Theme.Color.Text.dark
    paddingLeft = 10.px
  }
  val exitIcon by css {
    color = Theme.Color.Text.faded
    position = Position.absolute
    top = 10.px
    right = 10.px
  }
  val statusIcon by css {
    fontSize = 36.px
  }
  val error by css {
    borderLeft(5.px, BorderStyle.solid, Theme.Color.Indicator.error)
    color = Theme.Color.Indicator.error
  }
  val success by css {
    borderLeft(5.px, BorderStyle.solid, Theme.Color.Indicator.success)
    color = Theme.Color.Indicator.success
  }
  val warning by css {
    borderLeft(5.px, BorderStyle.solid, Theme.Color.Indicator.warning)
    color = Theme.Color.Indicator.warning
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)

private fun RBuilder.component(props: Props) {
  div(classes = cls(s.c { it::root }, s.c { if (props.open) it::open else it::closed }, props.status.styling)) {
    div(classes = s.c { it::statusIcon }) {
      inlineIcon(props.status.icon)
    }
    div(classes = s.c { it::content }) {
      b { +props.status.label }
      div { +props.message }
    }
    div(classes = s.c { it::exitIcon }) {
      attrs {
        onClickFunction = { props.onClose() }
      }
      inlineIcon("times")
    }
  }
}
