package io.limberapp.web.app

import io.limberapp.web.context.api.apiProvider
import io.limberapp.web.context.globalState.globalStateProvider
import io.limberapp.web.util.component
import react.*
import react.router.dom.*

/**
 * The application root. There are a number of components in this directory that start with "With". Each of them is part
 * of the application root, but unfortunately they need to be in different files due to the way providers and hooks
 * work. We can't call a hook to "use" something that is provided in the same component.
 */
internal fun RBuilder.app() {
  child(component)
}

private val component = component<RProps> component@{
  globalStateProvider {
    withAuth {
      apiProvider {
        browserRouter {
          switch {
            appRootRouter()
          }
        }
      }
    }
  }
}
