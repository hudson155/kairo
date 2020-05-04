package io.limberapp.web.app

import io.limberapp.web.context.globalState.globalStateProvider
import io.limberapp.web.context.theme.themeProvider
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

/**
 * Part of the application root.
 *   - Provides global state.
 *   - Provides theme.
 */
internal fun RBuilder.app() {
    child(app)
}

private val app = functionalComponent<RProps> {
    globalStateProvider {
        themeProvider {
            appWithAuth()
        }
    }
}
