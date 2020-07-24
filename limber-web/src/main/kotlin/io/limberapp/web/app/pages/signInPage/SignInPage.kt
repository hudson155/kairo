package io.limberapp.web.app.pages.signInPage

import io.limberapp.web.auth.useAuth
import react.*

/**
 * Redirects to sign in.
 */
internal fun RBuilder.signInPage() {
  child(component)
}

internal typealias Props = RProps

internal object SignInPage {
  const val name = "Sign in"
  const val path = "/signin"
}

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val auth = useAuth()
  auth.signIn()
}
