package io.limberapp.web.app.pages.signInPage

import io.limberapp.web.context.auth.useAuth
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

private val signInPage = functionalComponent<RProps> {
    val auth = useAuth()
    auth.signIn()
}

internal fun RBuilder.signInPage() {
    child(signInPage)
}
