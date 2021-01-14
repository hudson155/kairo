package io.limberapp.common.permissions.orgPermissions

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer

class OrgPermissionsDeserializer :
    StdScalarDeserializer<OrgPermissions>(OrgPermissions::class.java) {
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext) =
      OrgPermissions.fromDarb(p.text)
}
