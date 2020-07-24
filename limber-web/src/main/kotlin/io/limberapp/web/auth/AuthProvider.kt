package io.limberapp.web.auth

import io.limberapp.web.state.ProviderValue
import io.limberapp.web.state.state.tenant.useTenantState
import io.limberapp.web.util.async
import io.limberapp.web.util.external.AppState
import io.limberapp.web.util.external.Auth0Client
import io.limberapp.web.util.external.Auth0LogoutRequestProps
import io.limberapp.web.util.external.createAuth0Client
import io.limberapp.web.util.process
import io.limberapp.web.util.rootUrl
import kotlinext.js.jsObject
import kotlinx.coroutines.await
import react.*
import kotlin.browser.document
import kotlin.browser.window

internal fun RBuilder.authProvider(children: RHandler<Props>) {
  child(component, handler = children)
}

internal typealias Props = RProps

private val onRedirectCallback: (AppState?) -> Unit = {
  window.history.replaceState(
    data = jsObject {},
    title = document.title,
    url = it?.targetUrl ?: window.location.pathname
  )
}

private val authContext = createContext<AuthContext>()
internal fun useAuth() = useContext(authContext)

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val (tenant, _) = useTenantState()

  val (isLoading, setIsLoading) = useState(true)
  val (auth0Client, setAuth0Client) = useState<Auth0Client?>(null)
  val (isAuthenticated, setIsAuthenticated) = useState(false)
  val (jwt, setJwt) = useState<Jwt?>(null)

  useEffect(emptyList()) {
    async {
      val config = Auth0Config(
        domain = process.env.AUTH0_DOMAIN,
        client_id = tenant.auth0ClientId,
        redirect_uri = rootUrl,
        audience = "https://${process.env.AUTH0_DOMAIN}/api/v2/"
      )
      val client = createAuth0Client(config).await()
      setAuth0Client(client)
      if (window.location.search.contains("code=")) {
        val appState = client.handleRedirectCallback().await().appState
        onRedirectCallback(appState)
      }
      val isAuthenticatedWithAuth0 = client.isAuthenticated().await()
      setIsAuthenticated(isAuthenticatedWithAuth0)
      setJwt(if (isAuthenticatedWithAuth0) Jwt(client.getTokenSilently().await()) else null)
      setIsLoading(false)
    }
  }

  val configObject = ProviderValue(
    AuthContext(
      isLoading = isLoading,
      isAuthenticated = isAuthenticated,
      signIn = { checkNotNull(auth0Client).loginWithRedirect() },
      jwt = jwt,
      signOut = { checkNotNull(auth0Client).logout(Auth0LogoutRequestProps(rootUrl)) }
    )
  )
  child(createElement(authContext.Provider, configObject, props.children))
}

