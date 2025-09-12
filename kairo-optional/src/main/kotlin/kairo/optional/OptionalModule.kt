package kairo.optional

import kotlinx.serialization.modules.SerializersModule

public val optionalModule: SerializersModule =
  SerializersModule {
    contextual(Optional::class) { typeArgumentsSerializers ->
      OptionalSerializer(typeArgumentsSerializers[0])
    }
  }
