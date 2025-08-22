package kairo.rest.auth

import com.google.inject.Injector

@Suppress("UseDataClass")
public class AuthProvider(
  public val auth: Auth,
  public val injector: Injector,
)
