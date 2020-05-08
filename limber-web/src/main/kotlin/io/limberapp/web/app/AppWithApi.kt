package io.limberapp.web.app

import io.limberapp.web.context.api.apiProvider
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

/**
 * Part of the application root.
 *   - Provides API.
 */
internal fun RBuilder.appWithApi() {
    child(component)
}

private val component = functionalComponent<RProps> {
    apiProvider {
        appRouter()
    }
}
