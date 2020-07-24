package io.limberapp.web.app.components.modal

import io.limberapp.web.hook.useEscapeKeyListener
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*

/**
 * An area that shows over top of the page, dimming the background and showing covering the center of the page. The
 * dimmed background can be clicked to close the modal.
 *
 * If [blank] is set to true, no modal will be displayed, only the dimming background. This should only be used
 * transiently as something is loading.
 *
 * If [narrow] is set to true, the modal will be barely wide enough to fit the content. Otherwise it will be a
 * standardized width.
 *
 * [onClose] is called in order to close the modal. The modal can't remove itself from the DOM, so this is called to
 * make the parent do so.
 */
internal fun RBuilder.modal(
  blank: Boolean = false,
  narrow: Boolean = false,
  onClose: () -> Unit,
  children: RHandler<Props>
) {
  child(component, Props(blank, narrow, onClose), handler = children)
}

internal data class Props(val blank: Boolean, val narrow: Boolean, val onClose: () -> Unit) : RProps

private class S : Styles("Modal") {
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
    borderRadius = Theme.Sizing.borderRadius
    margin(16.px)
    padding(24.px)
    backgroundColor = Theme.Color.Background.light
  }
  val fixedWidthModal by css {
    width = 768.px
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  useEscapeKeyListener(emptyList()) { props.onClose() }

  div(classes = cls(s.c { it::fullScreen }, s.c { it::container })) {
    a(classes = cls(s.c { it::fullScreen }, s.c { it::fader })) {
      attrs.onClickFunction = { props.onClose() }
    }
    if (!props.blank) {
      div(classes = cls(s.c { it::modal }, if (!props.narrow) s.c { it::fixedWidthModal } else null)) {
        props.children()
      }
    }
  }
}
