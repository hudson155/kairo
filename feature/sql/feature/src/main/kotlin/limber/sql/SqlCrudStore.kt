package limber.sql

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.convertValue
import com.google.inject.Inject
import com.google.inject.name.Named
import limber.exception.UnprocessableException
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.bindKotlin
import org.jdbi.v3.core.result.ResultIterable
import org.jdbi.v3.core.statement.Query
import java.util.UUID
import kotlin.reflect.KClass

public abstract class SqlCrudStore<T : Any>(private val type: KClass<T>) : SqlStore() {
  protected abstract val tableName: String

  @Inject
  @Named(SQL_OBJECT_MAPPER)
  private lateinit var objectMapper: ObjectMapper

  protected fun Jdbi.create(model: T): T =
    transaction { handle ->
      val query = handle.createQuery(rs("store/crud/create.sql"))
      query.defineTableName()
      query.bindKotlin(document(model))
      return@transaction query.mapToType().single()
    }

  protected fun Jdbi.get(guid: UUID, forUpdate: Boolean = false): T? =
    handle { handle ->
      val query = handle.createQuery(rs("store/crud/get.sql"))
      query.defineTableName()
      query.define("lockingClause", if (forUpdate) "for update" else "")
      query.bind("guid", guid)
      return@handle query.mapToType().singleNullOrThrow()
    }

  protected fun Jdbi.update(guid: UUID, updater: (T) -> T): T =
    transaction { handle ->
      val model = updater(get(guid, forUpdate = true) ?: throw UnprocessableException())
      val query = handle.createQuery(rs("store/crud/update.sql"))
      query.defineTableName()
      query.bindKotlin(document(model))
      return@transaction query.mapToType().single()
    }

  private fun Query.defineTableName() {
    define("tableName", tableName)
  }

  private fun document(model: T): Document {
    val document = objectMapper.convertValue<ObjectNode>(model)
    val guid = UUID.fromString(document.remove("guid").textValue())
    return Document(guid, document)
  }

  private fun Query.mapToType(): ResultIterable<T> =
    mapTo(Document::class.java).map { document ->
      document.document.put("guid", document.guid.toString())
      return@map objectMapper.convertValue(document.document, type.java)
    }
}
