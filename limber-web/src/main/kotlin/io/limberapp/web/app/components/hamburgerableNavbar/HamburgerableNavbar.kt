package io.limberapp.web.app.components.hamburgerableNavbar

import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.web.app.components.hamburgerableNavbar.hamburgerableNavbarSubnav.hamburgerableNavbarSubnav
import io.limberapp.web.app.components.inlineIcon.inlineIcon
import io.limberapp.web.app.components.navbar.components.headerGroup.headerGroup
import io.limberapp.web.app.components.navbar.components.headerItem.headerItem
import io.limberapp.web.app.components.navbar.components.subnav.components.subnavItem.subnavItem
import io.limberapp.web.app.components.navbar.navbar
import io.limberapp.web.util.Styles
import io.limberapp.web.util.buildElements
import io.limberapp.web.util.c
import io.limberapp.web.util.cls
import io.limberapp.web.util.component
import io.limberapp.web.util.gs
import kotlinx.css.*
import kotlinx.css.properties.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*
import react.router.dom.*

/**
 * Generic top-of-page navbar that supports everything that [navbar] supports, but also has the ability to turn
 * [children] into the contents of a hamburger-accessible subnav when the screen size is small. [hamburgerableNavbar] is
 * also aware of the application-level concept of a [FeatureRep.Complete], and only allows these to be children, rather
 * than allowing arbitrary children. This design choice was made in order to easily accommodate the screen size
 * difference requiring each [FeatureRep.Complete] to map to either a [headerItem] or a [subnavItem].
 *
 * [left] is the same as it would be in [navbar], but will be hidden completely when the screen size is small.
 *
 * [right] is the same as it would be in [navbar].
 *
 * [features] are the features to render in the main part or hamburger part of the nav. Arbitrary nav or subnav content
 * is not supported.
 *
 * [hamburgerOpen] is whether or not the hamburger menu is open. This is managed externally in order to allow
 * coordination with other subnavs.
 *
 * [onHamburger] is called when the hamburger menu is opened or closed. It should re-render the [hamburgerableNavbar]
 * with a new [hamburgerOpen] value.
 */
internal fun RBuilder.hamburgerableNavbar(
  left: ReactElement?,
  right: ReactElement?,
  features: Set<FeatureRep.Complete>,
  hamburgerOpen: Boolean,
  onHamburger: (open: Boolean) -> Unit
) {
  child(component, Props(left, right, features, hamburgerOpen, onHamburger))
}

internal data class Props(
  val left: ReactElement?,
  val right: ReactElement?,
  val features: Set<FeatureRep.Complete>,
  val hamburgerOpen: Boolean,
  val onHamburger: (open: Boolean) -> Unit
) : RProps

private class S : Styles("HamburgerableNavbar") {
  val left by css {
    display = Display.flex
    alignItems = Align.center
  }
  val children by css {
    display = Display.flex
    alignItems = Align.center
  }
  val hamburger by css {
    display = Display.block
    height = 32.px
    width = 32.px
    lineHeight = 32.px.lh
    textAlign = TextAlign.center
    cursor = Cursor.pointer
  }
}

private val s = S().apply { inject() }

private val component = component<Props> component@{ props ->
  navbar(
    left = buildElements {
      div(classes = cls(gs.c { it::hiddenXs }, s.c { it::left })) { props.left?.let { child(it) } }
      div(classes = cls(gs.c { it::visibleXs }, s.c { it::left })) {
        headerGroup {
          if (props.hamburgerOpen) {
            hamburgerableNavbarSubnav(props.features, onUnfocus = { props.onHamburger(false) })
          }
          headerItem {
            a(classes = s.c { it::hamburger }) {
              attrs.onClickFunction = {
                props.onHamburger(!props.hamburgerOpen)
              }
              inlineIcon(if (!props.hamburgerOpen) "bars" else "times-circle")
            }
          }
        }
      }
    },
    right = props.right
  ) {
    div(classes = cls(gs.c { it::hiddenXs }, s.c { it::children })) {
      props.features.forEach { feature ->
        navLink<RProps>(to = feature.path) {
          attrs.key = feature.guid
          headerItem { +feature.name }
        }
      }
    }
  }
}
