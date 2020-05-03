package io.limberapp.web.app

import io.limberapp.web.context.globalState.globalStateProvider
import io.limberapp.web.context.theme.themeProvider
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

private val app = functionalComponent<RProps> {
    globalStateProvider {
        themeProvider {
            appWithAuth()
        }
    }
}

internal fun RBuilder.app() {
    child(app)
}
