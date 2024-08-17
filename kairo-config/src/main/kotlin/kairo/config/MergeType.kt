package kairo.config

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.node.ObjectNode

/**
 * Specifies how objects in configs should be merged, handled by [ConfigLoader].
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes(
  JsonSubTypes.Type(MergeType.Merge::class, name = "merge"),
  JsonSubTypes.Type(MergeType.Remove::class, name = "remove"),
  JsonSubTypes.Type(MergeType.Replace::class, name = "replace"),
)
internal sealed class MergeType {
  internal data class Merge @JsonCreator constructor(@JsonValue val value: ObjectNode) : MergeType()

  internal data object Remove : MergeType()

  internal data class Replace @JsonCreator constructor(@JsonValue val value: ObjectNode) : MergeType()
}
