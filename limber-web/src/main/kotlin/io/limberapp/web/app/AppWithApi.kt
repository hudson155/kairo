package io.limberapp.web.app

import io.limberapp.web.context.api.apiProvider
import io.limberapp.web.util.component
import react.*

/**
 * Part of the application root.
 *   - Provides API.
 */
internal fun RBuilder.appWithApi() {
  child(component)
}

private val component = component<RProps> component@{ _ ->
  apiProvider {
    appRouter()
  }
}
