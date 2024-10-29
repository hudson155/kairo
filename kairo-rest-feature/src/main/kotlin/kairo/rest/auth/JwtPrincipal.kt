package kairo.rest.auth

import com.auth0.jwt.interfaces.DecodedJWT
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import kairo.serialization.jsonMapper
import kairo.serialization.property.allowUnknownProperties

private val jwtMapper: JsonMapper =
  jsonMapper {
    allowUnknownProperties = true
  }

/**
 * WARNING: Be careful not to log sensitive data in this class.
 */
public class JwtPrincipal(
  private val decodedJwt: DecodedJWT,
) : Principal() {
  public inline fun <reified T : Any> getClaim(name: String): T? =
    getClaim(name, jacksonTypeRef())

  public fun <T : Any> getClaim(name: String, type: TypeReference<T>): T? {
    val claim = decodedJwt.getClaim(name)
    if (claim.isMissing || claim.isNull) return null
    @Suppress("ForbiddenMethodCall")
    return jwtMapper.readValue(claim.toString(), type)
  }

  override fun equals(other: Any?): Boolean {
    if (other !is JwtPrincipal) return false
    if (decodedJwt.token != other.decodedJwt.token) return false
    return true
  }

  override fun hashCode(): Int =
    decodedJwt.token.hashCode()
}
