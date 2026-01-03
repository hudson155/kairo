package kairo.serialization

import com.fasterxml.jackson.databind.module.SimpleModule
import kotlin.uuid.Uuid

internal class KairoKotlinModule : SimpleModule() {
  init {
    addSerializer(Uuid::class.java, KotlinUuidSerializer())
    addDeserializer(Uuid::class.java, KotlinUuidDeserializer())
  }
}
