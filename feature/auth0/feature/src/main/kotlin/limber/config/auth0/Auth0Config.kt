package limber.config.auth0

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import limber.type.ProtectedString

public data class Auth0Config(
  val managementApi: ManagementApi,
) {
  @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
  @JsonSubTypes(
    JsonSubTypes.Type(value = ManagementApi.Fake::class, name = "Fake"),
    JsonSubTypes.Type(value = ManagementApi.Real::class, name = "Real"),
  )
  public sealed class ManagementApi {
    public data object Fake : ManagementApi()

    public data class Real(
      val domain: String,
      val clientId: String,
      val clientSecret: ProtectedString,
    ) : ManagementApi()
  }
}
