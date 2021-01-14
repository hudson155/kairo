package io.limberapp.common.permissions.limberPermissions

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer

class LimberPermissionsDeserializer :
    StdScalarDeserializer<LimberPermissions>(LimberPermissions::class.java) {
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext) =
      LimberPermissions.fromDarb(p.text)
}
