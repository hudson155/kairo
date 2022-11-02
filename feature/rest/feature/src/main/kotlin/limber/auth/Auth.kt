package limber.auth

public abstract class Auth {
  public abstract fun authorize(context: RestContext): Boolean
}
