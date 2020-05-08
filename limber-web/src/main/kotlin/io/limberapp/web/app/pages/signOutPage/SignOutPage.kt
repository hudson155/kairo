package io.limberapp.web.app.pages.signOutPage

import io.limberapp.web.context.auth.useAuth
import io.limberapp.web.util.Page
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

/**
 * Redirects to sign out.
 */
internal fun RBuilder.signOutPage() {
    child(component)
}

internal val signOutPage = Page(
    name = "Sign out",
    path = "/signout"
)

private val component = functionalComponent<RProps> {
    val auth = useAuth()
    auth.signOut()
}
