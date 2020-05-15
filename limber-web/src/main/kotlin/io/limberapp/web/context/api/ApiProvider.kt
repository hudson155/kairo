package io.limberapp.web.context.api

import com.piperframework.restInterface.Fetch
import io.limberapp.web.context.ProviderValue
import io.limberapp.web.context.auth.useAuth
import io.limberapp.web.util.process
import react.*

/**
 * Provides the API for interaction with the backend.
 */
internal fun RBuilder.apiProvider(children: RHandler<RProps>) {
  child(component, handler = children)
}

private val api = createContext<Api>()
internal fun useApi() = useContext(api)

private val component = functionalComponent<RProps> { props ->
  val auth = useAuth()

  val fetch = object : Fetch(process.env.API_ROOT_URL, json) {
    override suspend fun headers(body: Boolean): dynamic {
      val headers = super.headers(body)
      if (auth.isAuthenticated) headers["Authorization"] = "Bearer ${checkNotNull(auth.jwt).raw}"
      return headers
    }
  }

  val configObject = ProviderValue(Api(fetch))
  child(createElement(api.Provider, configObject, props.children))
}
