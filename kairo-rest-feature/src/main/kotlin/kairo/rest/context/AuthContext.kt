package kairo.rest.context

import kairo.rest.auth.Auth
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.currentCoroutineContext

public class AuthContext(
  public val auth: Auth,
) : AbstractCoroutineContextElement(AuthContext) {
  public companion object : CoroutineContext.Key<AuthContext>
}

public suspend fun getAuthContext(): AuthContext? =
  currentCoroutineContext()[AuthContext]
