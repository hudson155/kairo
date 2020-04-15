package io.limberapp.web.context.api

import io.limberapp.web.app.components.navbar.components.headerLink.Props
import io.limberapp.web.context.ProviderValue
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
    val configObject = ProviderValue(Api())
    child(createElement(api.Provider, configObject, props.children))
}

internal fun RBuilder.apiProvider(children: RHandler<Props>) {
    child(apiProvider, handler = children)
}
