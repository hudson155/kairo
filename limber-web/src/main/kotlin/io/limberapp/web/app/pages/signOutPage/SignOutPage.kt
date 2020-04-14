package io.limberapp.web.app.pages.signOutPage

import io.limberapp.web.context.auth0.useAuth
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent

private val signOutPage = functionalComponent<RProps> {
    val auth = useAuth()
    auth.signOut()
}

internal fun RBuilder.signOutPage() {
    child(signOutPage)
}
