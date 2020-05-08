package io.limberapp.web.app.pages.signInPage

import io.limberapp.web.context.auth.useAuth
import io.limberapp.web.util.Page
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

/**
 * Redirects to sign in.
 */
internal fun RBuilder.signInPage() {
    child(component)
}

internal val signInPage = Page(
    name = "Sign in",
    path = "/signin"
)

private val component = functionalComponent<RProps> {
    val auth = useAuth()
    auth.signIn()
}
