package kairo.vertexAi.schema

import com.google.genai.types.Schema
import com.google.genai.types.Type
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Test

internal class VertexSchemaGeneratorTest {
  @Suppress("unused")
  internal enum class OperatingSystem { Linux, Mac, Windows }

  @Serializable
  internal sealed class AnimalSchema {
    internal abstract val name: String

    @Serializable
    @SerialName("Dog")
    internal data class DogSchema(override val name: String, val barksPerMinute: Int) : AnimalSchema()

    @Serializable
    @SerialName("Cat")
    internal data class CatSchema(override val name: String, val napsPerDay: Int) : AnimalSchema()
  }

  @Test
  fun array(): Unit =
    runTest {
      VertexSchemaGenerator.generate<List<String>>()
        .shouldBe(
          Schema.builder().apply {
            type(Type.Known.ARRAY)
            nullable(false)
            items(
              Schema.builder().apply {
                type(Type.Known.STRING)
                nullable(false)
              },
            )
          }.build(),
        )
    }

  @Test
  fun `array, nullable`(): Unit =
    runTest {
      VertexSchemaGenerator.generate<List<String?>>()
        .shouldBe(
          Schema.builder().apply {
            type(Type.Known.ARRAY)
            nullable(false)
            items(
              Schema.builder().apply {
                type(Type.Known.STRING)
                nullable(true)
              },
            )
          }.build(),
        )
    }
  @Test
  fun `array, with range`(): Unit =
    runTest {
      @Serializable
      data class TestSchema(
        @Vertex.Min(1.0)
        @Vertex.Max(7.0)
        val value: List<String>,
      )

      VertexSchemaGenerator.generate<TestSchema>()
        .shouldBe(
          Schema.builder().apply {
            type(Type.Known.OBJECT)
            nullable(false)
            properties(
              mapOf(
                "value" to Schema.builder().apply {
                  type(Type.Known.ARRAY)
                  minItems(1)
                  maxItems(7)
                  nullable(false)
                  items(
                    Schema.builder().apply {
                      type(Type.Known.STRING)
                      nullable(false)
                    },
                  )
                }.build(),
              ),
            )
            required("value")
          }.build(),
        )
    }

  @Test
  fun boolean(): Unit =
    runTest {
      VertexSchemaGenerator.generate<Boolean>()
        .shouldBe(
          Schema.builder().apply {
            type(Type.Known.BOOLEAN)
            nullable(false)
          }.build(),
        )
    }

  @Test
  fun double(): Unit =
    runTest {
      VertexSchemaGenerator.generate<Double>()
        .shouldBe(
          Schema.builder().apply {
            type(Type.Known.NUMBER)
            format("double")
            nullable(false)
          }.build(),
        )
    }

  @Test
  fun `double, with range`(): Unit =
    runTest {
      @Serializable
      data class TestSchema(
        @Vertex.Min(1.0)
        @Vertex.Max(7.0)
        val value: Double,
      )

      VertexSchemaGenerator.generate<TestSchema>()
        .shouldBe(
          Schema.builder().apply {
            type(Type.Known.OBJECT)
            nullable(false)
            properties(
              mapOf(
                "value" to Schema.builder().apply {
                  type(Type.Known.NUMBER)
                  format("double")
                  minimum(1.0)
                  maximum(7.0)
                  nullable(false)
                }.build(),
              ),
            )
            required("value")
          }.build(),
        )
    }

  @Test
  fun enum(): Unit =
    runTest {
      VertexSchemaGenerator.generate<OperatingSystem>()
        .shouldBe(
          Schema.builder().apply {
            type(Type.Known.STRING)
            enum_("Linux", "Mac", "Windows")
            nullable(false)
          }.build(),
        )
    }

  @Test
  fun float(): Unit =
    runTest {
      VertexSchemaGenerator.generate<Float>()
        .shouldBe(
          Schema.builder().apply {
            type(Type.Known.NUMBER)
            format("float")
            nullable(false)
          }.build(),
        )
    }

  @Test
  fun `float, with range`(): Unit =
    runTest {
      @Serializable
      data class TestSchema(
        @Vertex.Min(1.0)
        @Vertex.Max(7.0)
        val value: Float,
      )

      VertexSchemaGenerator.generate<TestSchema>()
        .shouldBe(
          Schema.builder().apply {
            type(Type.Known.OBJECT)
            nullable(false)
            properties(
              mapOf(
                "value" to Schema.builder().apply {
                  type(Type.Known.NUMBER)
                  format("float")
                  minimum(1.0)
                  maximum(7.0)
                  nullable(false)
                }.build(),
              ),
            )
            required("value")
          }.build(),
        )
    }

  @Test
  fun integer(): Unit =
    runTest {
      VertexSchemaGenerator.generate<Int>()
        .shouldBe(
          Schema.builder().apply {
            type(Type.Known.INTEGER)
            format("int32")
            nullable(false)
          }.build(),
        )
    }

  @Test
  fun `integer, with range`(): Unit =
    runTest {
      @Serializable
      data class TestSchema(
        @Vertex.Min(1.0)
        @Vertex.Max(7.0)
        val value: Int,
      )

      VertexSchemaGenerator.generate<TestSchema>()
        .shouldBe(
          Schema.builder().apply {
            type(Type.Known.OBJECT)
            nullable(false)
            properties(
              mapOf(
                "value" to Schema.builder().apply {
                  type(Type.Known.INTEGER)
                  format("int32")
                  minimum(1.0)
                  maximum(7.0)
                  nullable(false)
                }.build(),
              ),
            )
            required("value")
          }.build(),
        )
    }

  @Test
  fun long(): Unit =
    runTest {
      VertexSchemaGenerator.generate<Long>()
        .shouldBe(
          Schema.builder().apply {
            type(Type.Known.INTEGER)
            format("int64")
            nullable(false)
          }.build(),
        )
    }

  @Test
  fun `long, with range`(): Unit =
    runTest {
      @Serializable
      data class TestSchema(
        @Vertex.Min(1.0)
        @Vertex.Max(7.0)
        val value: Long,
      )

      VertexSchemaGenerator.generate<TestSchema>()
        .shouldBe(
          Schema.builder().apply {
            type(Type.Known.OBJECT)
            nullable(false)
            properties(
              mapOf(
                "value" to Schema.builder().apply {
                  type(Type.Known.INTEGER)
                  format("int64")
                  minimum(1.0)
                  maximum(7.0)
                  nullable(false)
                }.build(),
              ),
            )
            required("value")
          }.build(),
        )
    }

  @Test
  fun `object`(): Unit =
    runTest {
      @Serializable
      data class ChildSchema(
        val string: String,
      )

      @Serializable
      data class TestSchema(
        val child: ChildSchema,
        val boolean: Boolean,
        val enum: OperatingSystem,
        val integer: Int,
      )

      VertexSchemaGenerator.generate<TestSchema>()
        .shouldBe(
          Schema.builder().apply {
            type(Type.Known.OBJECT)
            nullable(false)
            properties(
              mapOf(
                "child" to Schema.builder().apply {
                  type(Type.Known.OBJECT)
                  nullable(false)
                  properties(
                    mapOf(
                      "string" to Schema.builder().apply {
                        type(Type.Known.STRING)
                        nullable(false)
                      }.build(),
                    ),
                  )
                  required("string")
                }.build(),
                "boolean" to Schema.builder().apply {
                  type(Type.Known.BOOLEAN)
                  nullable(false)
                }.build(),
                "enum" to Schema.builder().apply {
                  type(Type.Known.STRING)
                  enum_("Linux", "Mac", "Windows")
                  nullable(false)
                }.build(),
                "integer" to Schema.builder().apply {
                  type(Type.Known.INTEGER)
                  format("int32")
                  nullable(false)
                }.build(),
              ),
            )
            required("child", "boolean", "enum", "integer")
          }.build(),
        )
    }

  @Test
  fun `object, with descriptions`(): Unit =
    runTest {
      @Serializable
      @Vertex.Description("Child schema") // Overridden by "My child".
      data class ChildSchema(
        @Vertex.Description("My string")
        val string: String,
      )

      @Serializable
      @Vertex.Description("Test schema")
      data class TestSchema(
        @Vertex.Description("My child")
        val child: ChildSchema,
        @Vertex.Description("My boolean")
        val boolean: Boolean,
        @Vertex.Description("My enum")
        val enum: OperatingSystem,
        @Vertex.Description("My integer")
        val integer: Int,
      )

      VertexSchemaGenerator.generate<TestSchema>()
        .shouldBe(
          Schema.builder().apply {
            type(Type.Known.OBJECT)
            nullable(false)
            description("Test schema")
            properties(
              mapOf(
                "child" to Schema.builder().apply {
                  type(Type.Known.OBJECT)
                  nullable(false)
                  description("My child")
                  properties(
                    mapOf(
                      "string" to Schema.builder().apply {
                        type(Type.Known.STRING)
                        nullable(false)
                        description("My string")
                      }.build(),
                    ),
                  )
                  required("string")
                }.build(),
                "boolean" to Schema.builder().apply {
                  type(Type.Known.BOOLEAN)
                  nullable(false)
                  description("My boolean")
                }.build(),
                "enum" to Schema.builder().apply {
                  type(Type.Known.STRING)
                  enum_("Linux", "Mac", "Windows")
                  nullable(false)
                  description("My enum")
                }.build(),
                "integer" to Schema.builder().apply {
                  type(Type.Known.INTEGER)
                  format("int32")
                  nullable(false)
                  description("My integer")
                }.build(),
              ),
            )
            required("child", "boolean", "enum", "integer")
          }.build(),
        )
    }

  @Test
  fun `object, with nullables`(): Unit =
    runTest {
      @Serializable
      data class ChildSchema(
        val string: String?,
      )

      @Serializable
      data class TestSchema(
        val child: ChildSchema?,
        val boolean: Boolean?,
        val enum: OperatingSystem?,
        val integer: Int?,
      )

      VertexSchemaGenerator.generate<TestSchema>()
        .shouldBe(
          Schema.builder().apply {
            type(Type.Known.OBJECT)
            nullable(false)
            properties(
              mapOf(
                "child" to Schema.builder().apply {
                  type(Type.Known.OBJECT)
                  nullable(true)
                  properties(
                    mapOf(
                      "string" to Schema.builder().apply {
                        type(Type.Known.STRING)
                        nullable(true)
                      }.build(),
                    ),
                  )
                  required("string")
                }.build(),
                "boolean" to Schema.builder().apply {
                  type(Type.Known.BOOLEAN)
                  nullable(true)
                }.build(),
                "enum" to Schema.builder().apply {
                  type(Type.Known.STRING)
                  enum_("Linux", "Mac", "Windows")
                  nullable(true)
                }.build(),
                "integer" to Schema.builder().apply {
                  type(Type.Known.INTEGER)
                  format("int32")
                  nullable(true)
                }.build(),
              ),
            )
            required("child", "boolean", "enum", "integer")
          }.build(),
        )
    }

  @Test
  fun polymorphic(): Unit =
    runTest {
      @Serializable
      data class TestSchema(
        val animal: AnimalSchema,
      )

      VertexSchemaGenerator.generate<TestSchema>()
        .shouldBe(
          Schema.builder().apply {
            type(Type.Known.OBJECT)
            nullable(false)
            properties(
              mapOf(
                "animal" to Schema.builder().apply {
                  // Sealed classes seem to appear in alphabetical order; this is undocumented.
                  anyOf(
                    Schema.builder().apply {
                      type(Type.Known.OBJECT)
                      nullable(false)
                      properties(
                        mapOf(
                          "type" to Schema.builder().apply {
                            type(Type.Known.STRING)
                            enum_("Cat")
                            nullable(false)
                          }.build(),
                          "name" to Schema.builder().apply {
                            type(Type.Known.STRING)
                            nullable(false)
                          }.build(),
                          "napsPerDay" to Schema.builder().apply {
                            type(Type.Known.INTEGER)
                            format("int32")
                            nullable(false)
                          }.build(),
                        ),
                      )
                      required("type")
                      required("type", "name", "napsPerDay")
                    }.build(),
                    Schema.builder().apply {
                      type(Type.Known.OBJECT)
                      nullable(false)
                      properties(
                        mapOf(
                          "type" to Schema.builder().apply {
                            type(Type.Known.STRING)
                            enum_("Dog")
                            nullable(false)
                          }.build(),
                          "name" to Schema.builder().apply {
                            type(Type.Known.STRING)
                            nullable(false)
                          }.build(),
                          "barksPerMinute" to Schema.builder().apply {
                            type(Type.Known.INTEGER)
                            format("int32")
                            nullable(false)
                          }.build(),
                        ),
                      )
                      required("type")
                      required("type", "name", "barksPerMinute")
                    }.build(),
                  )
                }.build(),
              ),
            )
            required("animal")
          }.build(),
        )
    }

  @Test
  fun string(): Unit =
    runTest {
      VertexSchemaGenerator.generate<String>()
        .shouldBe(
          Schema.builder().apply {
            type(Type.Known.STRING)
            nullable(false)
          }.build(),
        )
    }

  @Test
  fun `string, format`(): Unit =
    runTest {
      @Serializable
      data class TestSchema(
        @Vertex.Format("email")
        val emailAddress: String,
      )

      VertexSchemaGenerator.generate<TestSchema>()
        .shouldBe(
          Schema.builder().apply {
            type(Type.Known.OBJECT)
            nullable(false)
            properties(
              mapOf(
                "emailAddress" to Schema.builder().apply {
                  type(Type.Known.STRING)
                  format("email")
                  nullable(false)
                }.build(),
              ),
            )
            required("emailAddress")
          }.build(),
        )
    }
}
