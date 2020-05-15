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
  child(component)
}

internal object SignOutPage {
  const val name = "Sign out"
  const val path = "/signout"
}

private val component = functionalComponent<RProps> {
  val auth = useAuth()
  auth.signOut()
}
