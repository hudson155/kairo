package io.limberapp.web.app

import io.limberapp.web.context.api.apiProvider
import io.limberapp.web.context.globalState.globalStateProvider
import io.limberapp.web.util.component
import react.*
import react.router.dom.*

/**
 * The application root.
 */
internal fun RBuilder.app() {
  child(component)
}

private val component = component<RProps> component@{
  globalStateProvider {
    withAuth {
      apiProvider {
        browserRouter {
          appRootRouter()
        }
      }
    }
  }
}
