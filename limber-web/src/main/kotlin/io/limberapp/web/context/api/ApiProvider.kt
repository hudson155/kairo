package io.limberapp.web.context.api

import com.piperframework.restInterface.Fetch
import io.limberapp.web.context.ProviderValue
import io.limberapp.web.context.auth0.useAuth
import io.limberapp.web.util.process
import kotlinx.coroutines.await
import react.RBuilder
import react.RHandler
import react.RProps
import react.child
import react.children
import react.createContext
import react.createElement
import react.functionalComponent
import react.useContext

private val api = createContext<Api>()
internal fun useApi() = useContext(api)

private val apiProvider = functionalComponent<RProps> { props ->

    val auth = useAuth()

    val fetch = object : Fetch(process.env.API_ROOT_URL) {
        override suspend fun headers(body: Boolean): dynamic {
            val headers = super.headers(body)
            if (auth.isAuthenticated) headers["Authorization"] = "Bearer ${auth.getJwt().await()}"
            return headers
        }
    }

    val configObject = ProviderValue(Api(fetch))
    child(createElement(api.Provider, configObject, props.children))
}

internal fun RBuilder.apiProvider(children: RHandler<RProps>) {
    this.child(apiProvider, handler = children)
}
