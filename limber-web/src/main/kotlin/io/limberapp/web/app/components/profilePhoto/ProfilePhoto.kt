package io.limberapp.web.app.components.profilePhoto

import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import kotlinx.css.*
import kotlinx.css.properties.*
import react.*
import react.dom.*

internal fun RBuilder.profilePhoto(
  placeholder: String,
  url: String?,
  small: Boolean = false,
  grayscale: Boolean = false,
  classes: String? = null,
) {
  child(component, Props(placeholder, url, small, grayscale, classes))
}

internal data class Props(
  val placeholder: String,
  val url: String?,
  val small: Boolean,
  val grayscale: Boolean,
  val classes: String?,
) : RProps

private class S : Styles("ProfilePhoto") {
  val container by css {
    position = Position.relative
    height = 48.px
    width = 48.px
    lineHeight = 48.px.lh
    textAlign = TextAlign.center
  }
  val small by css {
    height = 32.px
    width = 32.px
    lineHeight = 32.px.lh
  }
  val inner by css {
    position = Position.absolute
    boxSizing = BoxSizing.borderBox
    top = 0.px
    left = 0.px
    height = 100.pct
    width = 100.pct
    border(1.px, BorderStyle.dashed, Theme.Color.Border.dark)
    borderRadius = Theme.Sizing.borderRadius
  }
  val img by css {
    position = Position.absolute
    top = 0.px
    left = 0.px
    height = 100.pct
    width = 100.pct
    borderRadius = Theme.Sizing.borderRadius
  }
  val imgGrayscale by css {
    filter = "grayscale(100%)"
  }
}

private val s = S().apply { inject() }

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  div(classes = cls(s.c { it::container }, s.c(props.small) { it::small }, props.classes)) {
    div(classes = s.c { it::inner }) { +props.placeholder }
    props.url?.let { url ->
      img(src = url, classes = cls(s.c { it::img }, s.c(props.grayscale) { it::imgGrayscale })) {}
    }
  }
}
