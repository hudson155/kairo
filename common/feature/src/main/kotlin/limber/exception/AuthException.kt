package limber.exception

public class AuthException(public val status: Status) : Exception() {
  public enum class Status { Unauthorized, Forbidden }
}
