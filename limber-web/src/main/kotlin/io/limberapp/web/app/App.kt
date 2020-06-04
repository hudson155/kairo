package io.limberapp.web.app

import io.limberapp.web.util.component
import react.*

/**
 * Delegate for the application root.
 */
internal fun RBuilder.app() {
  child(component)
}

private val component = component<RProps> component@{
  appWithGlobalState()
}
