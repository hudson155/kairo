package io.limberapp.web.app

import io.limberapp.web.context.globalState.globalStateProvider
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

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
