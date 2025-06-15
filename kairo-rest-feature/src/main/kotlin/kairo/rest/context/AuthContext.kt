package kairo.rest.context

import kairo.rest.auth.Auth
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

public class AuthContext(
  public val auth: Auth,
) : AbstractCoroutineContextElement(AuthContext) {
  public companion object : CoroutineContext.Key<AuthContext>
}
