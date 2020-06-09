package io.limberapp.backend.authorization.permissions

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PrimitiveDescriptor
import kotlinx.serialization.PrimitiveKind
import kotlinx.serialization.Serializable

/**
 * Permissions that only apply to a specific organization feature.
 */
@Serializable(with = FeaturePermissionsSerializer::class)
class FeaturePermissions(override val permissions: Set<Nothing>) : Permissions<Nothing>() {
  override fun allPermissions() = emptyList<Nothing>()

  companion object {
    fun none() = FeaturePermissions(emptySet())

    @Suppress("UnusedPrivateMember")
    fun fromDarb(darb: String) = none()
  }
}

object FeaturePermissionsSerializer : KSerializer<FeaturePermissions> {
  override val descriptor = PrimitiveDescriptor("FeaturePermissions", PrimitiveKind.STRING)
  override fun serialize(encoder: Encoder, value: FeaturePermissions) = encoder.encodeString(value.asDarb())
  override fun deserialize(decoder: Decoder) = FeaturePermissions.fromDarb(decoder.decodeString())
}
