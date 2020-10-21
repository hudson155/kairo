package io.limberapp.common.permissions.featurePermissions

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import io.limberapp.common.permissions.Permissions
import io.limberapp.common.permissions.featurePermissions.feature.forms.FORMS_FEATURE_PREFIX
import io.limberapp.common.permissions.featurePermissions.feature.forms.FormsFeaturePermissions
import io.limberapp.common.permissions.featurePermissions.feature.forms.FormsFeaturePermissions.Companion.union
import io.limberapp.common.permissions.featurePermissions.feature.home.HOME_FEATURE_PREFIX
import io.limberapp.common.permissions.featurePermissions.feature.home.HomeFeaturePermissions
import io.limberapp.common.permissions.featurePermissions.feature.home.HomeFeaturePermissions.Companion.union
import io.limberapp.common.util.darb.BitStringEncoder
import io.limberapp.common.util.darb.DarbEncoder

/**
 * Permissions that only apply to a specific organization feature.
 */
@JsonDeserialize(using = FeaturePermissions.Deserializer::class)
abstract class FeaturePermissions : Permissions<FeaturePermission>() {
  companion object {
    fun fromDarb(darb: String) = darb.split('.', limit = 2).let {
      return@let fromBooleanList(it[0].single(), DarbEncoder.decode(it[1]))
    }

    fun fromBitString(bitString: String) = bitString.let {
      return@let fromBooleanList(it.first(), BitStringEncoder.decode(it.drop(1)))
    }

    private fun fromBooleanList(prefix: Char, booleanList: List<Boolean>) = when (prefix) {
      HOME_FEATURE_PREFIX -> HomeFeaturePermissions.fromBooleanList(booleanList)
      FORMS_FEATURE_PREFIX -> FormsFeaturePermissions.fromBooleanList(booleanList)
      else -> error("Unrecognized feature permissions prefix: $prefix.")
    }

    fun Collection<FeaturePermissions>.unionIfSameType(): FeaturePermissions? {
      val first = firstOrNull() ?: return null
      try {
        return when (first) {
          is FormsFeaturePermissions -> (this as Collection<FormsFeaturePermissions>).union()
          is HomeFeaturePermissions -> (this as Collection<HomeFeaturePermissions>).union()
          else -> unknownType("feature", first::class)
        }
      } catch (_: ClassCastException) {
        error("FeaturePermissions must be for the same feature type to do a union.")
      }
    }
  }

  class Deserializer : StdDeserializer<FeaturePermissions>(FeaturePermissions::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext) = fromDarb(p.text)
  }
}
