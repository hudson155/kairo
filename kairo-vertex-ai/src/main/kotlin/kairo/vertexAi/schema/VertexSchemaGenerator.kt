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
      is StructureKind.LIST -> generateList(descriptor, annotations)
      is PrimitiveKind.BOOLEAN -> generateBoolean(descriptor, annotations)
      is PrimitiveKind.INT -> generateNumber(descriptor, annotations, type = Type.Known.INTEGER, format = "int32")
      is PrimitiveKind.LONG -> generateNumber(descriptor, annotations, type = Type.Known.INTEGER, format = "int64")
      is PrimitiveKind.FLOAT -> generateNumber(descriptor, annotations, type = Type.Known.NUMBER, format = "float")
      is PrimitiveKind.DOUBLE -> generateNumber(descriptor, annotations, type = Type.Known.NUMBER, format = "double")
      is PrimitiveKind.STRING -> generateString(descriptor, annotations)
      else -> error("Unsupported kind (kind=${descriptor.kind}).")
    }

  private fun generateClass(descriptor: SerialDescriptor, annotations: List<Annotation>): Schema =
    Schema.builder().apply {
      type(Type.Known.OBJECT)
      nullable(descriptor.isNullable)
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

  private fun generateList(descriptor: SerialDescriptor, annotations: List<Annotation>): Schema {
    check(descriptor.elementsCount == 1) { "List must have exactly one element (descriptor=$descriptor)." }
    return Schema.builder().apply {
      type(Type.Known.ARRAY)
      annotation<Vertex.Min>(annotations)?.let { minItems(it.value.toLong()) }
      annotation<Vertex.Max>(annotations)?.let { maxItems(it.value.toLong()) }
      nullable(descriptor.isNullable)
      annotation<Vertex.Description>(annotations)?.let { description(it.value) }
      items(generate(descriptor.getElementDescriptor(0), annotations))
    }.build()
  }

  private fun generateBoolean(descriptor: SerialDescriptor, annotations: List<Annotation>): Schema =
    Schema.builder().apply {
      type(Type.Known.BOOLEAN)
      nullable(descriptor.isNullable)
      annotation<Vertex.Description>(annotations)?.let { description(it.value) }
    }.build()

  @Suppress("LongParameterList")
  private fun generateNumber(
    descriptor: SerialDescriptor,
    annotations: List<Annotation>,
    type: Type.Known,
    format: String,
  ): Schema =
    Schema.builder().apply {
      type(type)
      format(format)
      annotation<Vertex.Min>(annotations)?.let { minimum(it.value) }
      annotation<Vertex.Max>(annotations)?.let { maximum(it.value) }
      nullable(descriptor.isNullable)
      annotation<Vertex.Description>(annotations)?.let { description(it.value) }
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
