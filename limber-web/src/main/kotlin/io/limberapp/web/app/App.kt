package io.limberapp.web.app

import react.*

/**
 * Delegate for the application root.
 */
internal fun RBuilder.app() {
  child(component)
}

private val component = functionalComponent<RProps> {
  appWithGlobalState()
}
