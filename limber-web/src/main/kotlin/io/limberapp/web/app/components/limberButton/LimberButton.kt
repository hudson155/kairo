package io.limberapp.web.app.components.limberButton

import io.limberapp.web.app.components.loadingSpinner.loadingSpinner
import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import kotlinx.css.*
import kotlinx.css.properties.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*

/**
 * Limber styled button should generally be used in place of [button] in order to ensure a consistent UI across the app.
 *
 * The [style] allows for selecting the button color based on usage.
 *
 * [classes] is for CSS classes to apply.
 *
 * [children] can be any valid element to render inside a button. Generally this should be text.
 */
internal fun RBuilder.limberButton(
  style: Style,
  loading: Boolean = false,
  onClick: () -> Unit,
  classes: String? = null,
  children: RHandler<RProps>
) {
  child(component, Props(style, loading, onClick, classes), handler = children)
}

internal data class Props(
  val style: Style,
  val loading: Boolean,
  val onClick: () -> Unit,
  val classes: String?
) : RProps

internal enum class Style { PRIMARY, SECONDARY, DANGER }

private class S : Styles("LimberButton") {
  val base by css {
    color = Theme.Color.Text.light
    display = Display.grid
    fontSize = LinearDimension.initial
    fontWeight = FontWeight.bold
    border(1.px, BorderStyle.solid, Theme.Color.Border.light)
    borderRadius = 4.px
    padding(vertical = 6.px, horizontal = 12.px)
    cursor = Cursor.pointer
    width = LinearDimension.fitContent
  }
  val primary by css {
    backgroundColor = Theme.Color.Button.Primary.backgroundDefault
    hover {
      backgroundColor = Theme.Color.Button.Primary.backgroundActive
    }
    disabled {
      backgroundColor = Theme.Color.Button.Primary.backgroundDisabled
      hover {
        backgroundColor = Theme.Color.Button.Primary.backgroundDisabled
      }
    }
  }
  val secondary by css {
    backgroundColor = Theme.Color.Button.Secondary.backgroundDefault
    hover {
      backgroundColor = Theme.Color.Button.Secondary.backgroundActive
    }
    disabled {
      backgroundColor = Theme.Color.Button.Secondary.backgroundDisabled
      hover {
        backgroundColor = Theme.Color.Button.Secondary.backgroundDisabled
      }
    }
  }
  val danger by css {
    backgroundColor = Theme.Color.Button.Red.backgroundDefault
    hover {
      backgroundColor = Theme.Color.Button.Red.backgroundActive
    }
    disabled {
      backgroundColor = Theme.Color.Button.Red.backgroundDisabled
      hover {
        backgroundColor = Theme.Color.Button.Red.backgroundDisabled
      }
    }
  }
  val hidden by css {
    // Use visibility hidden to ensure the hidden components still take size within parent div. This ensures the button
    // won't resize when toggling between loading state.
    visibility = Visibility.hidden
  }
  val spinner by css {
    height = 0.px
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent<Props> { props ->
  val buttonStyle = when (props.style) {
    Style.PRIMARY -> s.c { it::primary }
    Style.SECONDARY -> s.c { it::secondary }
    Style.DANGER -> s.c { it::danger }
  }

  button(classes = cls(s.c{ it::base}, buttonStyle, props.classes)) {
    attrs.onClickFunction = { props.onClick() }
    attrs.disabled = props.loading

    span(classes = cls(s.c { it::spinner }, s.c(!props.loading) { it::hidden })) {
      loadingSpinner()
    }
    span(classes = s.c(props.loading) { it::hidden }) {
      props.children()
    }
  }
}
