package kairo.serialization

import com.fasterxml.jackson.databind.deser.Deserializers
import com.fasterxml.jackson.databind.module.SimpleDeserializers
import com.fasterxml.jackson.databind.module.SimpleModule

internal class PrimitivesModule : SimpleModule() {
  override fun setupModule(context: SetupContext) {
    super.setupModule(context)
    context.addDeserializers(buildDeserializers())
  }

  @Suppress("ComplexRedundantLet", "UnnecessaryLet")
  private fun buildDeserializers(): Deserializers =
    SimpleDeserializers().apply {
      BooleanDeserializer().let { deserializer ->
        addDeserializer(Boolean::class.javaPrimitiveType, deserializer)
        addDeserializer(Boolean::class.javaObjectType, deserializer)
      }
      DoubleDeserializer().let { deserializer ->
        addDeserializer(Double::class.javaPrimitiveType, deserializer)
        addDeserializer(Double::class.javaObjectType, deserializer)
      }
      FloatDeserializer().let { deserializer ->
        addDeserializer(Float::class.javaPrimitiveType, deserializer)
        addDeserializer(Float::class.javaObjectType, deserializer)
      }
      IntDeserializer().let { deserializer ->
        addDeserializer(Int::class.javaPrimitiveType, deserializer)
        addDeserializer(Int::class.javaObjectType, deserializer)
      }
      StringDeserializer().let { deserializer ->
        addDeserializer(String::class.javaObjectType, deserializer)
      }
      LongDeserializer().let { deserializer ->
        addDeserializer(Long::class.javaPrimitiveType, deserializer)
        addDeserializer(Long::class.javaObjectType, deserializer)
      }
    }
}

internal fun ObjectMapperFactoryBuilder.configurePrimitives() {
  addModule(PrimitivesModule())
}
