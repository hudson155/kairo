package io.limberapp.web.api

import io.limberapp.common.restInterface.ContentType
import io.limberapp.common.restInterface.Fetch
import io.limberapp.web.auth.useAuth
import io.limberapp.web.state.ProviderValue
import io.limberapp.web.util.process
import react.*

internal fun RBuilder.apiProvider(children: RHandler<Props>) {
  child(component, handler = children)
}

internal typealias Props = RProps

private val api = createContext<Api>()
internal fun useApi() = useContext(api)

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val auth = useAuth()

  val fetch = object : Fetch(process.env.API_ROOT_URL, json) {
    override suspend fun headers(body: Boolean, accept: ContentType): dynamic {
      val headers = super.headers(body, accept)
      if (auth.isAuthenticated) headers["Authorization"] = "Bearer ${checkNotNull(auth.jwt).raw}"
      return headers
    }
  }

  val configObject = ProviderValue(Api(fetch))
  child(createElement(api.Provider, configObject, props.children))
}
