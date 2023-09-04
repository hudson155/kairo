package limber.auth

import limber.exception.AuthException

public sealed class AuthResult {
  /**
   * Should be used when authorization information is missing.
   * For example, if no principal was provided but one was required,
   * or if a necessary claim was missing.
   */
  public class Unauthorized private constructor(private val message: String) : AuthResult() {
    public fun toException(): AuthException = AuthException(AuthException.Status.Unauthorized, message)

    public companion object {
      public fun noClaim(claimName: String): Unauthorized =
        Unauthorized("No $claimName claim on the provided token.")

      public fun noPrincipal(): Unauthorized =
        Unauthorized("No token provided.")
    }
  }

  /**
   * Should be used when authorization information was present,
   * but the user does not have permission to perform the action
   * (or does not have permission to perform the action on the particular entity).
   * For example, if the user is trying to delete an entity but does not have deletion permission.
   */
  public class Forbidden private constructor(private val message: String) : AuthResult() {
    public fun toException(): AuthException = AuthException(AuthException.Status.Forbidden, message)

    public companion object {
      public fun missingRequiredPermission(permission: String): Forbidden =
        Forbidden("Missing required permission $permission.")
    }
  }

  /**
   * Should be used when authorization succeeds.
   */
  public object Authorized : AuthResult()
}
