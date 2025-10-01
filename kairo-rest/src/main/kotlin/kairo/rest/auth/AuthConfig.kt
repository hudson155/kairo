package kairo.rest.auth

import io.ktor.server.auth.AuthenticationConfig
import kairo.rest.exception.Unauthorized

public abstract class AuthConfig {
  public abstract fun AuthenticationConfig.configure()

  public open suspend fun AuthReceiver<*>.default() {
    throw Unauthorized()
  }
}
