package io.limberapp.web.app.pages.notFoundPage

import io.limberapp.web.context.auth0.useAuth
import io.limberapp.web.util.async
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import react.useEffect
import styled.styledH1
import styled.styledP

private val notFoundPage = functionalComponent<RProps> {
    val auth = useAuth()
    useEffect {
        async {
            println(auth.getJwt())
        }
    }
    styledH1 { +"Not Found" }
    styledP { +"We looked everywhere, but we couldn't find the page you were looking for." }
}

internal fun RBuilder.notFoundPage() {
    child(notFoundPage)
}
