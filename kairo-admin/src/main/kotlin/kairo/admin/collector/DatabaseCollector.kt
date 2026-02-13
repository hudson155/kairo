package kairo.admin.collector

import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import kairo.admin.model.ColumnInfo
import kairo.admin.model.SqlQueryResult
import kairo.admin.model.TableInfo
import kairo.admin.sql.SqlValidator
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

public class DatabaseCollector(
  private val connectionFactoryProvider: () -> ConnectionFactory?,
) {
  private val sqlValidator: SqlValidator = SqlValidator()

  public suspend fun listTables(): List<TableInfo> {
    val factory = connectionFactoryProvider() ?: return emptyList()
    val connection = factory.create().awaitFirst()
    try {
      val sql = """
        SELECT table_schema, table_name
        FROM information_schema.tables
        WHERE table_schema NOT IN ('pg_catalog', 'information_schema')
          AND table_type = 'BASE TABLE'
        ORDER BY table_schema, table_name
      """.trimIndent()
      val result = connection.createStatement(sql).execute().awaitFirst()
      return result.map { row: Row, _: RowMetadata ->
        TableInfo(
          schema = row.get("table_schema", String::class.java) ?: "public",
          name = row.get("table_name", String::class.java).orEmpty(),
          columns = emptyList(),
        )
      }.awaitAll()
    } finally {
      withContext(NonCancellable) {
        connection.close().awaitFirstOrNull()
      }
    }
  }

  public suspend fun getTableColumns(schema: String, tableName: String): List<ColumnInfo> {
    val factory = connectionFactoryProvider() ?: return emptyList()
    val connection = factory.create().awaitFirst()
    try {
      val sql = """
        SELECT column_name, data_type, is_nullable, column_default, character_maximum_length
        FROM information_schema.columns
        WHERE table_schema = ${'$'}1 AND table_name = ${'$'}2
        ORDER BY ordinal_position
      """.trimIndent()
      val result = connection.createStatement(sql)
        .bind("${'$'}1", schema)
        .bind("${'$'}2", tableName)
        .execute()
        .awaitFirst()
      return result.map { row: Row, _: RowMetadata ->
        ColumnInfo(
          name = row.get("column_name", String::class.java).orEmpty(),
          dataType = row.get("data_type", String::class.java).orEmpty(),
          isNullable = row.get("is_nullable", String::class.java) == "YES",
          defaultValue = row.get("column_default", String::class.java),
          maxLength = row.get("character_maximum_length", Int::class.javaObjectType),
        )
      }.awaitAll()
    } finally {
      withContext(NonCancellable) {
        connection.close().awaitFirstOrNull()
      }
    }
  }

  @Suppress("TooGenericExceptionCaught", "LongMethod")
  public suspend fun executeQuery(sql: String): SqlQueryResult {
    val validationError = sqlValidator.validate(sql)
    if (validationError != null) {
      return SqlQueryResult(
        columns = emptyList(),
        rows = emptyList(),
        rowCount = 0,
        executionTimeMs = 0,
        error = validationError,
      )
    }
    val factory = connectionFactoryProvider()
      ?: return SqlQueryResult(emptyList(), emptyList(), 0, 0, "No database connection available.")
    val connection = factory.create().awaitFirst()
    try {
      connection.createStatement("SET TRANSACTION READ ONLY").execute().awaitFirst()
      val startTime = System.currentTimeMillis()
      val result = connection.createStatement(sql).execute().awaitFirst()
      val columns = mutableListOf<String>()
      val rows = mutableListOf<List<String?>>()
      result.map { row: Row, metadata: RowMetadata ->
        if (columns.isEmpty()) {
          metadata.columnMetadatas.forEach { columns.add(it.name) }
        }
        columns.map { colName -> row.get(colName)?.toString() }
      }.awaitAll().let { rows.addAll(it) }
      val executionTimeMs = System.currentTimeMillis() - startTime
      return SqlQueryResult(
        columns = columns,
        rows = rows,
        rowCount = rows.size,
        executionTimeMs = executionTimeMs,
      )
    } catch (e: CancellationException) {
      throw e
    } catch (e: Exception) {
      return SqlQueryResult(
        columns = emptyList(),
        rows = emptyList(),
        rowCount = 0,
        executionTimeMs = 0,
        error = e.message ?: "Unknown error",
      )
    } finally {
      withContext(NonCancellable) {
        connection.close().awaitFirstOrNull()
      }
    }
  }
}

/**
 * Awaits the first element from a [Publisher], suspending the coroutine.
 */
private suspend fun <T : Any> Publisher<T>.awaitFirst(): T =
  suspendCancellableCoroutine { cont ->
    subscribe(object : Subscriber<T> {
      private var subscription: Subscription? = null
      private var received = false

      override fun onSubscribe(s: Subscription) {
        subscription = s
        cont.invokeOnCancellation { s.cancel() }
        s.request(1)
      }

      override fun onNext(t: T) {
        if (!received) {
          received = true
          subscription?.cancel()
          cont.resume(t)
        }
      }

      override fun onError(t: Throwable) {
        if (!received) cont.resumeWithException(t)
      }

      override fun onComplete() {
        if (!received) cont.resumeWithException(NoSuchElementException("Publisher was empty."))
      }
    })
  }

/**
 * Awaits the first element from a [Publisher] or null if empty.
 */
private suspend fun <T : Any> Publisher<T>.awaitFirstOrNull(): T? =
  suspendCancellableCoroutine { cont ->
    subscribe(object : Subscriber<T> {
      private var subscription: Subscription? = null
      private var received = false

      override fun onSubscribe(s: Subscription) {
        subscription = s
        cont.invokeOnCancellation { s.cancel() }
        s.request(1)
      }

      override fun onNext(t: T) {
        if (!received) {
          received = true
          subscription?.cancel()
          cont.resume(t)
        }
      }

      override fun onError(t: Throwable) {
        if (!received) cont.resumeWithException(t)
      }

      override fun onComplete() {
        if (!received) cont.resume(null)
      }
    })
  }

/**
 * Collects all elements from a [Publisher] into a [List].
 */
private suspend fun <T : Any> Publisher<T>.awaitAll(): List<T> =
  suspendCancellableCoroutine { cont ->
    val items = mutableListOf<T>()
    subscribe(object : Subscriber<T> {
      override fun onSubscribe(s: Subscription) {
        cont.invokeOnCancellation { s.cancel() }
        s.request(Long.MAX_VALUE)
      }

      override fun onNext(t: T) {
        items.add(t)
      }

      override fun onError(t: Throwable) {
        cont.resumeWithException(t)
      }

      override fun onComplete() {
        cont.resume(items)
      }
    })
  }
