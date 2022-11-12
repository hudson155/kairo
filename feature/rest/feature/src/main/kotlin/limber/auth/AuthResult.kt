package limber.auth

public sealed class AuthResult {
  /**
   * Should be used when authorization information is missing.
   * For example, if no principal was provided but one was required,
   * or if a necessary claim was missing.
   */
  public data class Unauthorized(val message: String) : AuthResult() {
    public companion object {
      public fun noClaim(claimName: String): Unauthorized =
        Unauthorized("No $claimName claim on the provided token.")

      public fun noPrincipal(): Unauthorized =
        Unauthorized("No token provided.")
    }
  }

  /**
   * Should be used when authorization information was present,
   * but the user does not have permission to perform the action.
   * For example, if the user is trying to delete an entity but does not have deletion permission.
   *
   * If the user is trying to perform an action on an entity they do not have access to,
   * use [Failed] instead.
   */
  public data class Forbidden(val message: String) : AuthResult()

  /**
   * Should be used when authorization succeeds.
   */
  public object Authorized : AuthResult()

  /**
   * Should be used when the user tries to perform an action on an entity they do not have access to.
   * For example, if the user has deletion permission for an entity
   * but is trying to delete an entity they don't have access to.
   *
   * If the user does not have permission to perform the action,
   * use [Forbidden] instead.
   */
  public object Failed : AuthResult()
}
