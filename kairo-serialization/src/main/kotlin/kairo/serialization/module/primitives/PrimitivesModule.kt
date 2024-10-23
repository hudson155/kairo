package kairo.serialization.module.primitives

import com.fasterxml.jackson.databind.module.SimpleModule
import kotlin.uuid.Uuid

/**
 * Although this is named "primitives", it doesn't just handle primitives.
 * For example, [String] and [Uuid] are not primitives.
 *
 * Regarding UUIDs, Jackson supports [java.util.UUID] by default, but not [kotlin.uuid.Uuid] which we use.
 */
@Suppress("LeakingThis")
internal abstract class PrimitivesModule(
  protected val trimWhitespace: TrimWhitespace.Type,
) : SimpleModule() {
  init {
    configureBoolean()
    configureDouble()
    configureFloat()
    configureInt()
    configureLong()
    configureString()
    configureUuid()
  }

  protected abstract fun configureBoolean()

  protected abstract fun configureDouble()

  protected abstract fun configureFloat()

  protected abstract fun configureInt()

  protected abstract fun configureLong()

  private fun configureString() {
    addSerializer(String::class.javaObjectType, StringSerializer())
    addKeySerializer(String::class.javaObjectType, StringSerializer.Key())
    addDeserializer(String::class.javaObjectType, StringDeserializer(trimWhitespace = trimWhitespace))
    addKeyDeserializer(String::class.javaObjectType, StringDeserializer.Key())
  }

  private fun configureUuid() {
    addSerializer(Uuid::class.javaObjectType, UuidSerializer())
    addKeySerializer(Uuid::class.javaObjectType, UuidSerializer.Key())
    addDeserializer(Uuid::class.javaObjectType, UuidDeserializer())
    addKeyDeserializer(Uuid::class.javaObjectType, UuidDeserializer.Key())
  }
}
