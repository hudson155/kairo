package limber.auth

public abstract class Auth {
  /**
   * Indicates that an endpoint is publicly accessible.
   * Authentication is not required to access the endpoint.
   * Therefore, the endpoint will work even if no JWT is provided at all.
   */
  public object Public : Auth() {
    override fun authorize(context: RestContext): AuthResult = AuthResult.Authorized
  }

  public abstract fun authorize(context: RestContext): AuthResult
}
