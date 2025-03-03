package kairo.rest.auth

import com.auth0.jwt.interfaces.DecodedJWT
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.json.JsonMapper
import io.github.projectmapk.jackson.module.kogera.jacksonTypeRef
import kairo.serialization.jsonMapper
import kairo.serialization.property.allowUnknownProperties

private val jwtMapper: JsonMapper =
  jsonMapper {
    allowUnknownProperties = true
  }.build()

/**
 * WARNING: Be careful not to log sensitive data in this class.
 */
public class JwtPrincipal(
  private val decodedJwt: DecodedJWT,
) : Principal() {
  public inline fun <reified T : Any> getClaim(name: String): T? =
    getClaim(name, jacksonTypeRef())

  @Suppress("ForbiddenMethodCall")
  public fun <T : Any> getClaim(name: String, type: TypeReference<T>): T? {
    val claim = decodedJwt.getClaim(name)
    if (claim.isMissing || claim.isNull) return null
    return jwtMapper.readerFor(type).readValue(claim.toString())
  }

  override fun equals(other: Any?): Boolean {
    if (other !is JwtPrincipal) return false
    if (decodedJwt.token != other.decodedJwt.token) return false
    return true
  }

  override fun hashCode(): Int =
    decodedJwt.token.hashCode()
}
