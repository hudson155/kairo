package kairo.rest.auth

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
  JsonSubTypes.Type(AuthVerifierConfig.Jwt::class, name = "Jwt"),
)
public sealed class AuthVerifierConfig {
  public abstract val schemes: List<String>

  public data class Jwt(
    override val schemes: List<String>,
    val mechanisms: List<JwtAuthMechanismConfig>,
  ) : AuthVerifierConfig()
}
