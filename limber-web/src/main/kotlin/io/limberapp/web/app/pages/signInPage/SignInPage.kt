package io.limberapp.web.app.pages.signInPage

import io.limberapp.web.context.auth.useAuth
import io.limberapp.web.util.component
import react.*

/**
 * Redirects to sign in.
 */
internal fun RBuilder.signInPage() {
  child(component)
}

internal object SignInPage {
  const val name = "Sign in"
  const val path = "/signin"
}

private val component = component<RProps> component@{ _ ->
  val auth = useAuth()
  auth.signIn()
}
