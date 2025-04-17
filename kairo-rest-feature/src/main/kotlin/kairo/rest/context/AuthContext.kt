package kairo.rest.context

import kairo.rest.auth.Auth
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

public class AuthContext(
  public val auth: Auth,
) : AbstractCoroutineContextElement(key) {
  public companion object {
    internal val key: CoroutineContext.Key<AuthContext> =
      object : CoroutineContext.Key<AuthContext> {}
  }
}

public suspend fun getAuthContext(): AuthContext? =
  coroutineContext[AuthContext.key]
