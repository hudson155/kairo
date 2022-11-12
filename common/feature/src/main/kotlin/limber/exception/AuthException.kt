package limber.exception

public class AuthException(
  public val status: Status,
  public val userMessage: String,
) : Exception("Authorization returned $status. $userMessage") {
  public enum class Status { Unauthorized, Forbidden }
}
