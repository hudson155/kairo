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
        val integer: Int,
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
                "integer" to Schema.builder().apply {
                  type(Type.Known.INTEGER)
                  nullable(false)
                }.build(),
              ),
            )
            required("child", "boolean", "integer")
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
        @Vertex.Description("My integer")
        val integer: Int,
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
                "integer" to Schema.builder().apply {
                  type(Type.Known.INTEGER)
                  nullable(false)
                  description("My integer")
                }.build(),
              ),
            )
            required("child", "boolean", "integer")
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
        val integer: Int?,
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
                "integer" to Schema.builder().apply {
                  type(Type.Known.INTEGER)
                  nullable(true)
                }.build(),
              ),
            )
            required("child", "boolean", "integer")
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
