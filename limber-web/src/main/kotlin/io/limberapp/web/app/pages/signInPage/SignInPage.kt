package io.limberapp.web.app.pages.signInPage

import io.limberapp.web.context.auth.useAuth
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

/**
 * Redirects to sign in.
 */
internal fun RBuilder.signInPage() {
    child(signInPage)
}

private val signInPage = functionalComponent<RProps> {
    val auth = useAuth()
    auth.signIn()
}
