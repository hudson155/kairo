package io.limberapp.web.app.pages.signOutPage

import io.limberapp.web.context.auth.useAuth
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

/**
 * Redirects to sign out.
 */
internal fun RBuilder.signOutPage() {
    child(signOutPage)
}

private val signOutPage = functionalComponent<RProps> {
    val auth = useAuth()
    auth.signOut()
}
