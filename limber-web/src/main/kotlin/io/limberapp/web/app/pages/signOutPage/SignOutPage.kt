package io.limberapp.web.app.pages.signOutPage

import io.limberapp.web.context.auth.useAuth
import io.limberapp.web.util.component
import react.*

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

private val component = component<RProps> component@{ _ ->
  val auth = useAuth()
  auth.signOut()
}
