package kairo.rest.auth

import io.ktor.server.auth.AuthenticationConfig

public abstract class AuthConfig {
  public abstract fun AuthenticationConfig.configure()

  public open suspend fun AuthReceiver<*>.default() {
    error("This endpoint must implement auth.")
  }
}
