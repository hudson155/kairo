package io.limberapp.web.app

import io.limberapp.web.api.apiProvider
import io.limberapp.web.context.globalState.globalStateProvider
import react.*
import react.router.dom.*

/**
 * The application root.
 */
internal fun RBuilder.app() {
  child(component)
}

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: RProps) {
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
