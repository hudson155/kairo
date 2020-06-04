package io.limberapp.web.app.components.profilePhoto

import io.limberapp.web.util.Styles
import io.limberapp.web.util.Theme
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import io.limberapp.web.util.component
import kotlinx.css.*
import kotlinx.css.properties.*
import react.*
import react.dom.*

/**
 * A user's profile photo.
 *
 * [placeholder] is some text to be used in place of the profile photo if it's null and/or while it's loading.
 *
 * [url] is the URL to pull the profile photo from. It can be any URL. It's not validated.
 *
 * If [small] is true, the profile photo will display in a small version. Otherwise it will display in a large version.
 * The small version should be used in a navbar, for example.
 *
 * If [grayscale] is true, the profile photo will be shown in grayscale. This should be used to represent a disabled
 * state.
 *
 * [classes] is for CSS classes to apply.
 */
internal fun RBuilder.profilePhoto(
  placeholder: String,
  url: String?,
  small: Boolean = false,
  grayscale: Boolean = false,
  classes: String? = null
) {
  child(component, Props(placeholder, url, small, grayscale, classes))
}

internal data class Props(
  val placeholder: String,
  val url: String?,
  val small: Boolean,
  val grayscale: Boolean,
  val classes: String?
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

private val component = component<Props> component@{ props ->
  div(classes = cls(s.c { it::container }, s.c(props.small) { it::small }, props.classes)) {
    div(classes = s.c { it::inner }) { +props.placeholder }
    props.url?.let { url ->
      img(src = url, classes = cls(s.c { it::img }, s.c(props.grayscale) { it::imgGrayscale })) {}
    }
  }
}
