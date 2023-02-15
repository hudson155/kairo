package limber.auth

import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.databind.node.TextNode
import io.ktor.server.auth.jwt.JWTPrincipal
import java.util.UUID

public const val PERMISSIONS_CLAIM_NAME: String = "permissions"

@JsonDeserialize(using = PermissionValue.Deserializer::class)
public sealed class PermissionValue {
  public abstract operator fun contains(guid: UUID): Boolean

  public object All : PermissionValue() {
    override fun contains(guid: UUID): Boolean = true

    @JsonValue
    override fun toString(): String = "*"
  }

  public data class Some(@JsonValue val guids: Set<UUID>) : PermissionValue() {
    override fun contains(guid: UUID): Boolean = guid in guids
  }

  internal class Deserializer : StdDeserializer<PermissionValue>(PermissionValue::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): PermissionValue? {
      when (val json = p.readValueAsTree<JsonNode>()) {
        is NullNode -> return null
        is TextNode -> {
          require(json.textValue() == "*") // The only supported string is "*".
          return All
        }
        is ArrayNode -> return Some(json.map { UUID.fromString(it.textValue()) }.toSet())
        else -> error("Unsupported JsonNode type: ${json::class.simpleName}.")
      }
    }
  }
}

public fun RestContext.getPermissions(principal: JWTPrincipal): Permissions? =
  getClaim(principal, PERMISSIONS_CLAIM_NAME)
