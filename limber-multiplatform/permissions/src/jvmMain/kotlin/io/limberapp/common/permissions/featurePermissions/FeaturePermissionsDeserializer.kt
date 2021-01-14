package io.limberapp.common.permissions.featurePermissions

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer

abstract class FeaturePermissionsDeserializer(
    private val types: Map<Char, (darb: String) -> FeaturePermissions<*>>,
) : StdScalarDeserializer<FeaturePermissions<*>>(FeaturePermissions::class.java) {
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): FeaturePermissions<*> {
    val prefixedDarb = p.text
    val (prefix) = prefixedDarb.split('.', limit = 2)
    val type = types[prefix.singleOrNull() ?: error("$prefix is not a valid prefix.")]
        ?: error("No type found for prefix $prefix.")
    return type(prefixedDarb)
  }
}
