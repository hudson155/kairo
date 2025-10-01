package kairo.rest.auth

import io.ktor.server.auth.AuthenticationConfig
import kairo.rest.AuthReceiver

public abstract class AuthConfig {
  public abstract fun AuthenticationConfig.configure()

  public abstract suspend fun AuthReceiver<*>.default()
}
