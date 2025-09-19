package kairo.vertexAi.schema

import com.google.genai.types.Schema
import com.google.genai.types.Type
import kotlin.reflect.typeOf
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.serializer

public object VertexSchemaGenerator {
  public inline fun <reified T : Any> generate(): Schema =
    generate(serializer(typeOf<T>()).descriptor)

  public fun generate(descriptor: SerialDescriptor, annotations: List<Annotation> = emptyList()): Schema =
    @Suppress("ElseCaseInsteadOfExhaustiveWhen")
    when (descriptor.kind) {
      is StructureKind.CLASS -> generateClass(descriptor, annotations)
      is PrimitiveKind.BOOLEAN -> generateBoolean(descriptor, annotations)
      is PrimitiveKind.STRING -> generateString(descriptor, annotations)
      else -> error("Unsupported kind (kind=${descriptor.kind}).")
    }

  private fun generateBoolean(descriptor: SerialDescriptor, annotations: List<Annotation>): Schema =
    Schema.builder().apply {
      type(Type.Known.BOOLEAN)
      nullable(descriptor.isNullable)
      annotation<Vertex.Description>(annotations)?.let { description(it.value) }
    }.build()

  private fun generateClass(descriptor: SerialDescriptor, annotations: List<Annotation>): Schema =
    Schema.builder().apply {
      type(Type.Known.OBJECT)
      annotation<Vertex.Description>(descriptor.annotations + annotations)?.let { description(it.value) }
      properties(
        List(descriptor.elementsCount) { i ->
          Pair(
            first = descriptor.getElementName(i),
            second = generate(descriptor.getElementDescriptor(i), descriptor.getElementAnnotations(i)),
          )
        }.toMap(),
      )
      required(List(descriptor.elementsCount) { i -> descriptor.getElementName(i) })
    }.build()

  private fun generateString(descriptor: SerialDescriptor, annotations: List<Annotation>): Schema =
    Schema.builder().apply {
      type(Type.Known.STRING)
      annotation<Vertex.Format>(annotations)?.let { format(it.value) }
      nullable(descriptor.isNullable)
      annotation<Vertex.Description>(annotations)?.let { description(it.value) }
    }.build()

  private inline fun <reified T : Annotation> annotation(annotations: List<Annotation>): T? =
    annotations.filterIsInstance<T>().lastOrNull()
}
