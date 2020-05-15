package io.limberapp.web.app.components.modal

import io.limberapp.web.hook.useEscapeKeyListener
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.classes
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*
import styled.getClassName

internal fun RBuilder.modal(blank: Boolean = false, onClose: () -> Unit, children: RHandler<Props>) {
  child(component, Props(blank, onClose), handler = children)
}

internal data class Props(val blank: Boolean, val onClose: () -> Unit) : RProps

private val styles = object : Styles("Modal") {
  val fullScreen by css {
    position = Position.absolute
    top = 0.px
    left = 0.px
    height = 100.pct
    width = 100.pct
  }
  val container by css {
    display = Display.flex
    flexDirection = FlexDirection.column
    alignItems = Align.center
    justifyContent = JustifyContent.center
  }
  val fader by css {
    zIndex = Theme.ZIndex.modalFader
    backgroundColor = Theme.Color.Background.dark.withAlpha(0.5)
    cursor = Cursor.default
  }
  val modal by css {
    zIndex = Theme.ZIndex.modalModal
    width = 768.px
    borderRadius = Theme.Sizing.borderRadius
    margin(16.px)
    padding(24.px)
    backgroundColor = Theme.Color.Background.light
  }
}.apply { inject() }

private val component = functionalComponent<Props> { props ->
  useEscapeKeyListener(emptyList()) { props.onClose() }

  div(
    classes = classes(
      styles.getClassName { it::fullScreen },
      styles.getClassName { it::container }
    )
  ) {
    a(
      classes = classes(
        styles.getClassName { it::fullScreen },
        styles.getClassName { it::fader }
      )
    ) {
      attrs.onClickFunction = { props.onClose() }
    }
    if (!props.blank) {
      div(classes = styles.getClassName { it::modal }) {
        props.children()
      }
    }
  }
}
