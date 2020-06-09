package io.limberapp.backend.authorization.permissions.featurePermissions

import com.piperframework.util.darb.BitStringEncoder
import com.piperframework.util.darb.DarbEncoder
import io.limberapp.backend.authorization.permissions.Permissions
import io.limberapp.backend.authorization.permissions.featurePermissions.feature.forms.FORMS_FEATURE_PREFIX
import io.limberapp.backend.authorization.permissions.featurePermissions.feature.forms.FormsFeaturePermissions
import io.limberapp.backend.authorization.permissions.featurePermissions.feature.home.HOME_FEATURE_PREFIX
import io.limberapp.backend.authorization.permissions.featurePermissions.feature.home.HomeFeaturePermissions
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
abstract class FeaturePermissions : Permissions<FeaturePermission>() {
  companion object {
    fun fromDarb(prefix: Char, darb: String) = fromBooleanList(prefix, DarbEncoder.decode(darb))

    fun fromBitString(prefix: Char, bitString: String) = fromBooleanList(prefix, BitStringEncoder.decode(bitString))

    private fun fromBooleanList(prefix: Char, booleanList: List<Boolean>) = when (prefix) {
      HOME_FEATURE_PREFIX -> HomeFeaturePermissions.fromBooleanList(booleanList)
      FORMS_FEATURE_PREFIX -> FormsFeaturePermissions.fromBooleanList(booleanList)
      else -> error("Unrecognized feature permissions prefix: $prefix.")
    }
  }
}

object FeaturePermissionsSerializer : KSerializer<FeaturePermissions> {
  override val descriptor = PrimitiveDescriptor("FeaturePermissions", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, value: FeaturePermissions) =
    encoder.encodeString(value.toString())

  override fun deserialize(decoder: Decoder) = decoder.decodeString().split('.').let {
    return@let FeaturePermissions.fromDarb(it[0].single(), it[1])
  }
}
