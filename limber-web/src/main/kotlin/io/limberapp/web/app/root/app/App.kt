package io.limberapp.web.app.root.app

import com.piperframework.restInterface.Fetch
import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.web.api.Api
import io.limberapp.web.api.apiProvider
import io.limberapp.web.api.json
import io.limberapp.web.api.load
import io.limberapp.web.app.components.minimalPage.LinkType
import io.limberapp.web.app.components.minimalPage.minimalPage
import io.limberapp.web.app.pages.failedToLoadPage.failedToLoadPage
import io.limberapp.web.app.pages.loadingPage.loadingPage
import io.limberapp.web.app.root.appRootRouter.appRootRouter
import io.limberapp.web.auth.authProvider
import io.limberapp.web.state.state.tenant.tenantStateProvider
import io.limberapp.web.util.Theme
import io.limberapp.web.util.process
import io.limberapp.web.util.rootDomain
import react.*
import react.router.dom.*
import kotlin.browser.document

/**
 * The root of the application.
 */
internal fun RBuilder.app() {
  child(component)
}

internal typealias Props = RProps

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  // We use a non-authenticated API here rather than calling the useApi() hook which we should do everywhere else
  // because the tenant must be fetched before we can create the AuthProvider, and the AuthProvider is required for
  // the ApiProvider.
  val nonAuthenticatedApi = Api(Fetch(process.env.API_ROOT_URL, json))

  val tenant = load { nonAuthenticatedApi(TenantApi.GetByDomain(rootDomain)) }

  // Set theme elements.
  useEffect(listOf(Theme.Color.Background.light)) {
    checkNotNull(document.body).style.color = Theme.Color.Text.dark.value
    checkNotNull(document.body).style.backgroundColor = Theme.Color.Background.light.value
  }

  // While the tenant is loading, show the loading page.
  (tenant ?: return minimalPage(linkType = null) { loadingPage("Loading tenant...") })
    .onFailure { return minimalPage(linkType = LinkType.SIGN_OUT) { failedToLoadPage("tenant") } }

  tenantStateProvider(tenant.value) {
    authProvider {
      apiProvider {
        browserRouter {
          appRootRouter()
        }
      }
    }
  }
}
