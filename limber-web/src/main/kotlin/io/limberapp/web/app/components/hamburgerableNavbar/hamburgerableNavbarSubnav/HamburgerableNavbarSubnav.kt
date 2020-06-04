package io.limberapp.web.app.components.hamburgerableNavbar.hamburgerableNavbarSubnav

import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.web.app.components.mainAppNavbar.mainAppNavbar
import io.limberapp.web.app.components.navbar.components.subnav.components.subnavGroup.subnavGroup
import io.limberapp.web.app.components.navbar.components.subnav.components.subnavItem.subnavItem
import io.limberapp.web.app.components.navbar.components.subnav.subnav
import io.limberapp.web.hook.useClickListener
import io.limberapp.web.util.component
import react.*
import react.router.dom.*

/**
 * Subnav on the [mainAppNavbar] that shows up when the hamburger is clicked.
 *
 * [features] are the features to render. Arbitrary subnav content is not supported
 *
 * [onUnfocus] is the function to call when there's a click outside of this element. Normally, calling this function
 * should hide the subnav.
 */
internal fun RBuilder.hamburgerableNavbarSubnav(features: Set<FeatureRep.Complete>, onUnfocus: () -> Unit) {
  child(component, Props(features, onUnfocus))
}

internal data class Props(val features: Set<FeatureRep.Complete>, val onUnfocus: () -> Unit) : RProps

private val component = component<Props> component@{ props ->
  useClickListener(emptyList()) { props.onUnfocus() }

  subnav {
    props.features.forEach { feature ->
      subnavGroup {
        navLink<RProps>(to = feature.path) {
          attrs.key = feature.guid
          subnavItem { +feature.name }
        }
      }
    }
  }
}
