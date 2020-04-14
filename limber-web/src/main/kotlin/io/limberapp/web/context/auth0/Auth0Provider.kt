package io.limberapp.web.context.auth0

import io.limberapp.web.context.ProviderValue
import io.limberapp.web.util.AppState
import io.limberapp.web.util.Auth0Client
import io.limberapp.web.util.Auth0LogoutRequestProps
import io.limberapp.web.util.async
import io.limberapp.web.util.createAuth0Client
import io.limberapp.web.util.process
import io.limberapp.web.util.rootUrl
import kotlinext.js.asJsObject
import kotlinx.coroutines.await
import react.RBuilder
import react.RHandler
import react.RProps
import react.children
import react.createContext
import react.createElement
import react.functionalComponent
import react.useContext
import react.useEffect
import react.useState
import kotlin.browser.window

private val authContext = createContext<Auth0Context>()
internal fun useAuth() = useContext(authContext)

internal data class Props(val clientId: String, val onRedirectCallback: (AppState?) -> Unit) : RProps

private val authProvider = functionalComponent<Props> { props ->

    val (isLoading, setIsLoading) = useState(true)
    val (auth0Client, setAuth0Client) = useState<Auth0Client?>(null)
    val (isAuthenticated, setIsAuthenticated) = useState(false)

    useEffect(emptyList()) {
        async {
            val config = Auth0Config(
                domain = process.env.AUTH0_DOMAIN,
                client_id = props.clientId,
                redirect_uri = rootUrl,
                audience = "https://${process.env.AUTH0_DOMAIN}/api/v2/"
            )
            val client = createAuth0Client(config.asJsObject()).await()
            setAuth0Client(client)
            if (window.location.search.contains("code=")) {
                val appState = client.handleRedirectCallback().await().appState
                props.onRedirectCallback(appState)
            }
            setIsAuthenticated(client.isAuthenticated().await())
            setIsLoading(false)
        }
    }

    val configObject = ProviderValue(
        Auth0Context(
            isLoading = isLoading,
            isAuthenticated = isAuthenticated,
            signIn = { checkNotNull(auth0Client).loginWithRedirect() },
            getJwt = { checkNotNull(auth0Client).getTokenSilently().await() },
            signOut = {
                checkNotNull(auth0Client).logout(Auth0LogoutRequestProps(rootUrl).asJsObject())
            }
        )
    )
    child(createElement(authContext.Provider, configObject, props.children))
}

internal fun RBuilder.authProvider(
    clientId: String,
    onRedirectCallback: (AppState?) -> Unit,
    children: RHandler<Props>
) {
    child(authProvider, Props(clientId, onRedirectCallback), handler = children)
}

