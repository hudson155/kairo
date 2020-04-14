package io.limberapp.web

import io.limberapp.web.app.app
import io.limberapp.web.context.globalState.stateProvider
import react.dom.render
import kotlin.browser.document

internal fun main() {
    render(document.getElementById("root")) {
        stateProvider { app() }
    }
}
