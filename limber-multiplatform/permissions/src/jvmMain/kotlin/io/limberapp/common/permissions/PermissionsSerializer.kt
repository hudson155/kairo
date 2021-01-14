package io.limberapp.common.permissions

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer

class PermissionsSerializer : StdScalarSerializer<Permissions<*>>(Permissions::class.java) {
  override fun serialize(value: Permissions<*>, gen: JsonGenerator, provider: SerializerProvider) {
    gen.writeString(value.asDarb())
  }
}
