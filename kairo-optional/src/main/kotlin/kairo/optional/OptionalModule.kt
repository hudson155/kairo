package kairo.optional

import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModule

public fun optionalModule(): SerializersModule =
  SerializersModule {
    contextual(Required::class) { typeArgumentsSerializers ->
      @Suppress("UNCHECKED_CAST")
      RequiredSerializer(typeArgumentsSerializers[0] as KSerializer<Any>)
    }
    contextual(Optional::class) { typeArgumentsSerializers ->
      @Suppress("UNCHECKED_CAST")
      OptionalSerializer(typeArgumentsSerializers[0] as KSerializer<Any>)
    }
  }
