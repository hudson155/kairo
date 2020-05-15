package io.limberapp.web.app.components.modal

import io.limberapp.web.hook.useEscapeKeyListener
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.classes
import kotlinx.css.Align
import kotlinx.css.Cursor
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.JustifyContent
import kotlinx.css.Position
import kotlinx.css.alignItems
import kotlinx.css.backgroundColor
import kotlinx.css.borderRadius
import kotlinx.css.cursor
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.css.height
import kotlinx.css.justifyContent
import kotlinx.css.left
import kotlinx.css.margin
import kotlinx.css.padding
import kotlinx.css.pct
import kotlinx.css.position
import kotlinx.css.px
import kotlinx.css.top
import kotlinx.css.width
import kotlinx.css.zIndex
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RHandler
import react.RProps
import react.dom.a
import react.dom.div
import react.functionalComponent
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
