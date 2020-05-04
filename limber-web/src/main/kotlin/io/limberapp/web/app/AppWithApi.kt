package io.limberapp.web.app

import io.limberapp.web.context.api.apiProvider
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

internal fun RBuilder.appWithApi() {
    child(appWithApi)
}

private val appWithApi = functionalComponent<RProps> {
    apiProvider {
        appRouter()
    }
}
