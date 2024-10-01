package kairo.rest.auth

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import kairo.protectedString.ProtectedString

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
  JsonSubTypes.Type(JwtAuthMechanismConfig.Jwt::class, name = "Jwt"),
)
public sealed class JwtAuthMechanismConfig {
  public abstract val issuers: List<String?>

  public abstract val leewaySec: Long

  public data class Jwt(
    override val issuers: List<String?>,
    val algorithm: Algorithm,
    override val leewaySec: Long,
  ) : JwtAuthMechanismConfig() {
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes(
      JsonSubTypes.Type(Algorithm.Hmac256::class, name = "Hmac256"),
    )
    public sealed class Algorithm {
      public data class Hmac256(
        val secret: ProtectedString,
      ) : Algorithm()
    }
  }
}
