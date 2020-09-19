package io.limberapp.backend.authorization.permissions.featurePermissions

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import io.limberapp.backend.authorization.permissions.Permissions
import io.limberapp.backend.authorization.permissions.featurePermissions.feature.forms.FORMS_FEATURE_PREFIX
import io.limberapp.backend.authorization.permissions.featurePermissions.feature.forms.FormsFeaturePermissions
import io.limberapp.backend.authorization.permissions.featurePermissions.feature.home.HOME_FEATURE_PREFIX
import io.limberapp.backend.authorization.permissions.featurePermissions.feature.home.HomeFeaturePermissions
import io.limberapp.util.darb.BitStringEncoder
import io.limberapp.util.darb.DarbEncoder

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
  }

  class Deserializer : StdDeserializer<FeaturePermissions>(FeaturePermissions::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext) = fromDarb(p.text)
  }
}
