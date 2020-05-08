package io.limberapp.web.app

import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

/**
 * Delegate for the application root.
 */
internal fun RBuilder.app() {
    child(component)
}

private val component = functionalComponent<RProps> {
    appWithGlobalState()
}
