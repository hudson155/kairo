package io.limberapp.web.app

import com.piperframework.restInterface.Fetch
import io.limberapp.web.app.components.minimalPage.minimalPage
import io.limberapp.web.app.pages.failedToLoadPage.failedToLoadPage
import io.limberapp.web.app.pages.loadingPage.loadingPage
import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.api.Api
import io.limberapp.web.context.api.json
import io.limberapp.web.context.auth.authProvider
import io.limberapp.web.context.globalState.action.tenant.loadTenant
import io.limberapp.web.util.Theme
import io.limberapp.web.util.componentWithGlobalState
import io.limberapp.web.util.external.AppState
import io.limberapp.web.util.process
import io.limberapp.web.util.rootDomain
import kotlinext.js.jsObject
import react.*
import kotlin.browser.document
import kotlin.browser.window

/**
 * Part of the application root. Loads the tenant and provides auth, but doesn't guarantee that auth is loaded.
 */
internal fun RBuilder.withAuth(children: RHandler<RProps>) {
  child(component, handler = children)
}

private val onRedirectCallback: (AppState?) -> Unit = {
  window.history.replaceState(
    data = jsObject {},
    title = document.title,
    url = it?.targetUrl ?: window.location.pathname
  )
}

private val component = componentWithGlobalState<RProps> component@{ self, props ->
  // We use a non-authenticated API here rather than calling the useApi() hook which we should do everywhere else
  // because the tenant must be fetched before we can create the AuthProvider, and the AuthProvider is required for
  // the ApiProvider.
  val nonAuthenticatedApi = Api(Fetch(process.env.API_ROOT_URL, json))

  self.loadTenant(nonAuthenticatedApi, rootDomain)

  // Set theme elements
  useEffect(listOf(Theme.Color.Background.light)) {
    checkNotNull(document.body).style.color = Theme.Color.Text.dark.value
    checkNotNull(document.body).style.backgroundColor = Theme.Color.Background.light.value
  }

  // While the tenant is loading, show the loading page.
  val tenant = self.gs.tenant.let { loadableState ->
    when (loadableState) {
      is LoadableState.Unloaded -> return@component minimalPage(linkType = null) { loadingPage("Loading tenant...") }
      is LoadableState.Error -> return@component minimalPage(linkType = null) { failedToLoadPage("tenant") }
      is LoadableState.Loaded -> return@let loadableState.state
    }
  }

  authProvider(
    clientId = tenant.auth0ClientId,
    onRedirectCallback = onRedirectCallback
  ) {
    props.children()
  }
}
