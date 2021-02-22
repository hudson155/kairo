package io.limberapp.common.typeConversion

import io.limberapp.common.permissions.limberPermissions.LimberPermission
import io.limberapp.common.permissions.limberPermissions.LimberPermissions
import io.limberapp.common.permissions.orgPermissions.OrgPermission
import io.limberapp.common.permissions.orgPermissions.OrgPermissions
import io.limberapp.common.sql.IntegrationTest
import io.limberapp.common.typeConversion.typeConverter.FeaturePermissionsTypeConverter
import io.limberapp.common.typeConversion.typeConverter.LimberPermissionsTypeConverter
import io.limberapp.common.typeConversion.typeConverter.OrgPermissionsTypeConverter
import io.limberapp.common.typeConversion.typeConverter.RegexTypeConverter
import io.limberapp.common.typeConversion.typeConverter.UuidTypeConverter
import org.jdbi.v3.core.mapper.NoSuchMapperException
import org.jdbi.v3.core.statement.UnableToCreateStatementException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class TypeConverterInstallerTest : IntegrationTest() {
  private class Value<V : Any>(val raw: V, val string: String)

  private sealed class WithoutTypeConverter<E : Exception> {
    class Success<E : Exception> : WithoutTypeConverter<E>()

    class Failure<E : Exception>(
        val expectation: (E) -> Unit,
    ) : WithoutTypeConverter<E>()
  }

  @BeforeEach
  override fun beforeEach() {
    super.beforeEach()
    jdbi.useHandle<Exception> { handle ->
      handle.createUpdate("DROP TABLE IF EXISTS type_converter_test")
          .execute()
    }
  }

  @Test
  fun `LimberPermissions - write`() {
    val value = LimberPermissions(setOf(LimberPermission.IDENTITY_PROVIDER))
    writeTest(
        value = Value(raw = value, string = value.asDarb()),
        typeConverter = LimberPermissionsTypeConverter,
        withoutTypeConverter = WithoutTypeConverter.Failure { e ->
          val message = assertNotNull(e.message)
          assertTrue(
              Regex("^No argument factory registered for .*" +
                  " of qualified type .*LimberPermissions.*").containsMatchIn(message),
              message = message)
        },
    )
  }

  @Test
  fun `LimberPermissions - read`() {
    val value = LimberPermissions(setOf(LimberPermission.IDENTITY_PROVIDER))
    readTest(
        value = Value(raw = value, string = value.asDarb()),
        typeConverter = LimberPermissionsTypeConverter,
        withoutTypeConverter = WithoutTypeConverter.Failure { e ->
          val message = assertNotNull(e.message)
          assertTrue(
              Regex("^No mapper registered for type" +
                  " .*LimberPermissions").containsMatchIn(message),
              message = message)
        },
    )
  }

  @Test
  fun `OrgPermissions - write`() {
    val value = OrgPermissions(setOf(OrgPermission.MODIFY_OWN_METADATA))
    writeTest(
        value = Value(raw = value, string = value.asDarb()),
        typeConverter = OrgPermissionsTypeConverter,
        withoutTypeConverter = WithoutTypeConverter.Failure { e ->
          val message = assertNotNull(e.message)
          assertTrue(
              Regex("^No argument factory registered for .*" +
                  " of qualified type .*OrgPermissions.*").containsMatchIn(message),
              message = message)
        },
    )
  }

  @Test
  fun `OrgPermissions - read`() {
    val value = OrgPermissions(setOf(OrgPermission.MODIFY_OWN_METADATA))
    readTest(
        value = Value(raw = value, string = value.asDarb()),
        typeConverter = OrgPermissionsTypeConverter,
        withoutTypeConverter = WithoutTypeConverter.Failure { e ->
          val message = assertNotNull(e.message)
          assertTrue(
              Regex("^No mapper registered for type" +
                  " .*OrgPermissions").containsMatchIn(message),
              message = message)
        },
    )
  }

  @Test
  fun `FeaturePermissions - write`() {
    val value = TestFeaturePermissions(setOf(TestFeaturePermission.TEST_FEATURE_PERMISSION_2))
    writeTest(
        value = Value(raw = value, string = value.asDarb()),
        typeConverter = FeaturePermissionsTypeConverter(mapOf('T' to TestFeaturePermissions)),
        withoutTypeConverter = WithoutTypeConverter.Failure { e ->
          val message = assertNotNull(e.message)
          assertTrue(
              Regex("^No argument factory registered for .*" +
                  " of qualified type .*FeaturePermissions.*").containsMatchIn(message),
              message = message)
        },
    )
  }

  @Test
  fun `FeaturePermissions - read`() {
    val value = TestFeaturePermissions(setOf(TestFeaturePermission.TEST_FEATURE_PERMISSION_2))
    readTest(
        value = Value(raw = value, string = value.asDarb()),
        typeConverter = FeaturePermissionsTypeConverter(mapOf('T' to TestFeaturePermissions)),
        withoutTypeConverter = WithoutTypeConverter.Failure { e ->
          val message = assertNotNull(e.message)
          assertTrue(
              Regex("^No mapper registered for type" +
                  " .*FeaturePermissions").containsMatchIn(message),
              message = message)
        },
    )
  }

  @Test
  fun `Regex - write`() {
    val value = Regex("end[.]$")
    writeTest(
        value = Value(raw = value, string = value.toString()),
        typeConverter = RegexTypeConverter,
        withoutTypeConverter = WithoutTypeConverter.Failure { e ->
          val message = assertNotNull(e.message)
          assertTrue(
              Regex("^No argument factory registered for .*" +
                  " of qualified type .*Regex.*").containsMatchIn(message),
              message = message)
        },
    )
  }

  @Test
  fun `Regex - read`() {
    val value = Regex("end[.]$")
    readTest(
        value = Value(raw = value, string = value.toString()),
        typeConverter = RegexTypeConverter,
        withoutTypeConverter = WithoutTypeConverter.Failure { e ->
          val message = assertNotNull(e.message)
          assertTrue(
              Regex("^No mapper registered for type" +
                  " .*Regex").containsMatchIn(message),
              message = message)
        },
        comparisonMapper = { it.pattern },
    )
  }

  @Test
  fun `UUID - write`() {
    val value = UUID.randomUUID()
    writeTest(
        value = Value(raw = value, string = value.toString()),
        typeConverter = UuidTypeConverter,
        withoutTypeConverter = WithoutTypeConverter.Success(),
    )
  }

  @Test
  fun `UUID - read`() {
    val value = UUID.randomUUID()
    readTest(
        value = Value(raw = value, string = value.toString()),
        typeConverter = UuidTypeConverter,
        withoutTypeConverter = WithoutTypeConverter.Success(),
    )
  }

  private fun <V : Any> writeTest(
      value: Value<V>,
      typeConverter: TypeConverter<V>,
      withoutTypeConverter: WithoutTypeConverter<UnableToCreateStatementException>,
  ) {
    createTable()

    when (withoutTypeConverter) {
      is WithoutTypeConverter.Success -> run {
        attemptInsert(value.raw)
        val result = getResult<String>()
        assertEquals(value.string, result)
        truncateTable()
      }
      is WithoutTypeConverter.Failure -> run {
        assertFailsWith<UnableToCreateStatementException> {
          attemptInsert(value.raw)
        }.let(withoutTypeConverter.expectation)
      }
    }

    TypeConverterInstaller.install(jdbi, setOf(typeConverter))

    attemptInsert(value.raw)
    val result = getResult<String>()
    assertEquals(value.string, result)
  }

  private inline fun <reified V : Any> readTest(
      value: Value<V>,
      typeConverter: TypeConverter<V>,
      withoutTypeConverter: WithoutTypeConverter<NoSuchMapperException>,
  ) {
    readTest(value, typeConverter, withoutTypeConverter) { it }
  }

  @Suppress("LongParameterList")
  private inline fun <reified V : Any, C : Any> readTest(
      value: Value<V>,
      typeConverter: TypeConverter<V>,
      withoutTypeConverter: WithoutTypeConverter<NoSuchMapperException>,
      comparisonMapper: (V) -> C,
  ) {
    createTable()
    attemptInsert(value.string)

    when (withoutTypeConverter) {
      is WithoutTypeConverter.Success -> run<Unit> {
        val result = getResult<V>()
        assertEquals(comparisonMapper(value.raw), comparisonMapper(result))
      }
      is WithoutTypeConverter.Failure -> run<Unit> {
        assertFailsWith<NoSuchMapperException> {
          getResult<V>()
        }.let(withoutTypeConverter.expectation)
      }
    }

    TypeConverterInstaller.install(jdbi, setOf(typeConverter))

    val result = getResult<V>()
    assertEquals(comparisonMapper(value.raw), comparisonMapper(result))
  }

  private fun createTable() {
    jdbi.useHandle<Exception> { handle ->
      handle.createUpdate("CREATE TABLE type_converter_test (col TEXT)")
          .execute()
    }
  }

  private fun truncateTable() {
    jdbi.useHandle<Exception> { handle ->
      handle.createUpdate("TRUNCATE type_converter_test")
          .execute()
    }
  }

  private fun <V : Any> attemptInsert(value: V) {
    jdbi.useHandle<Exception> { handle ->
      handle.createUpdate("INSERT INTO type_converter_test (col) VALUES (:value)")
          .bind("value", value)
          .execute()
    }
  }

  private inline fun <reified V : Any> getResult(): V =
      jdbi.withHandle<V, Exception> { handle ->
        handle.createQuery("SELECT * FROM type_converter_test")
            .mapTo(V::class.java)
            .single()
      }
}
