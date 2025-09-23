package kairo.vertexAi.schema

import com.google.genai.types.Schema
import com.google.genai.types.Type
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Test

@Suppress("LongMethod")
internal class VertexSchemaGeneratorTest {
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
  fun `object`(): Unit =
    runTest {
      @Serializable
      data class ChildSchema(
        val boolean: Boolean,
      )

      @Serializable
      data class TestSchema(
        val child: ChildSchema,
        val string: String,
      )

      VertexSchemaGenerator.generate<TestSchema>()
        .shouldBe(
          Schema.builder().apply {
            type(Type.Known.OBJECT)
            properties(
              mapOf(
                "child" to Schema.builder().apply {
                  type(Type.Known.OBJECT)
                  properties(
                    mapOf(
                      "boolean" to Schema.builder().apply {
                        type(Type.Known.BOOLEAN)
                        nullable(false)
                      }.build(),
                    ),
                  )
                  required("boolean")
                }.build(),
                "string" to Schema.builder().apply {
                  type(Type.Known.STRING)
                  nullable(false)
                }.build(),
              ),
            )
            required("child", "string")
          }.build(),
        )
    }

  @Test
  fun `object, with descriptions`(): Unit =
    runTest {
      @Serializable
      @Vertex.Description("Child schema") // Overridden by "My child".
      data class ChildSchema(
        @Vertex.Description("My boolean")
        val boolean: Boolean,
      )

      @Serializable
      @Vertex.Description("Test schema")
      data class TestSchema(
        @Vertex.Description("My child")
        val child: ChildSchema,
        @Vertex.Description("My string")
        val string: String,
      )

      VertexSchemaGenerator.generate<TestSchema>()
        .shouldBe(
          Schema.builder().apply {
            type(Type.Known.OBJECT)
            description("Test schema")
            properties(
              mapOf(
                "child" to Schema.builder().apply {
                  type(Type.Known.OBJECT)
                  description("My child")
                  properties(
                    mapOf(
                      "boolean" to Schema.builder().apply {
                        type(Type.Known.BOOLEAN)
                        nullable(false)
                        description("My boolean")
                      }.build(),
                    ),
                  )
                  required("boolean")
                }.build(),
                "string" to Schema.builder().apply {
                  type(Type.Known.STRING)
                  nullable(false)
                  description("My string")
                }.build(),
              ),
            )
            required("child", "string")
          }.build(),
        )
    }

  @Test
  fun `object, with nullables`(): Unit =
    runTest {
      @Serializable
      data class TestSchema(
        val boolean: Boolean?,
        val string: String?,
      )

      VertexSchemaGenerator.generate<TestSchema>()
        .shouldBe(
          Schema.builder().apply {
            type(Type.Known.OBJECT)
            properties(
              mapOf(
                "boolean" to Schema.builder().apply {
                  type(Type.Known.BOOLEAN)
                  nullable(true)
                }.build(),
                "string" to Schema.builder().apply {
                  type(Type.Known.STRING)
                  nullable(true)
                }.build(),
              ),
            )
            required("boolean", "string")
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
}
