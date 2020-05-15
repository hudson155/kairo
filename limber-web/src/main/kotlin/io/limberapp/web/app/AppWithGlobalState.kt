package io.limberapp.web.app

import io.limberapp.web.context.globalState.globalStateProvider
import react.*

/**
 * Part of the application root.
 *   - Provides global state.
 */
internal fun RBuilder.appWithGlobalState() {
  child(component)
}

private val component = functionalComponent<RProps> {
  globalStateProvider {
    appWithAuth()
  }
}
