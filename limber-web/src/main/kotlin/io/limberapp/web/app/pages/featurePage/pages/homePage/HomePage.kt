package io.limberapp.web.app.pages.featurePage.pages.homePage

import io.limberapp.web.app.components.layout.components.layoutTitle.layoutTitle
import io.limberapp.web.app.components.layout.components.standardLayout.standardLayout
import io.limberapp.web.util.component
import react.*
import react.dom.*

/**
 * Placeholder homepage.
 */
internal fun RBuilder.homePage() {
  child(component)
}

internal object HomePage {
  const val name = "Home"
}

private val component = component<RProps> component@{
  standardLayout {
    layoutTitle(HomePage.name)
    p { +"This is the homepage." }
  }
}
