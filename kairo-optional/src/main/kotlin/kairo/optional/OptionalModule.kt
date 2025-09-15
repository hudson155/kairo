package kairo.optional

import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModule

public val optionalModule: SerializersModule =
  SerializersModule {
    contextual(Optional::class) { typeArgumentsSerializers ->
      @Suppress("UNCHECKED_CAST")
      OptionalSerializer(typeArgumentsSerializers[0] as KSerializer<Any>)
    }
  }
