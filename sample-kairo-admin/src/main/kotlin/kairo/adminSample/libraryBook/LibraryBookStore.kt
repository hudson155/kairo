package kairo.adminSample.libraryBook

import kotlin.time.Clock
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.deleteWhere
import org.jetbrains.exposed.v1.r2dbc.insert
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.r2dbc.update

public class LibraryBookStore(
  private val database: R2dbcDatabase,
) {
  public suspend fun get(id: LibraryBookId): LibraryBookModel? =
    suspendTransaction(database) {
      LibraryBookTable
        .selectAll()
        .where { LibraryBookTable.id eq id.value }
        .map { LibraryBookTable.fromRow(it) }
        .singleOrNull()
    }

  public suspend fun listAll(): List<LibraryBookModel> =
    suspendTransaction(database) {
      LibraryBookTable
        .selectAll()
        .map { LibraryBookTable.fromRow(it) }
        .toList()
    }

  public suspend fun create(creator: LibraryBookModel.Creator): LibraryBookModel {
    val bookId = LibraryBookId.random()
    suspendTransaction(database) {
      LibraryBookTable.insert { statement ->
        statement[id] = bookId.value
        statement[title] = creator.title
        statement[isbn] = creator.isbn
        statement[genre] = creator.genre
      }
    }
    return get(bookId) ?: error("Failed to create library book.")
  }

  public suspend fun update(id: LibraryBookId, update: LibraryBookModel.Update): LibraryBookModel? {
    val now = Clock.System.now()
    val updatedCount = suspendTransaction(database) {
      LibraryBookTable.update({ LibraryBookTable.id eq id.value }) { statement ->
        update.title?.let { value -> statement[title] = value }
        update.isbn?.let { value -> statement[isbn] = value }
        update.genre?.let { value -> statement[genre] = value }
        statement[updatedAt] = now
      }
    }
    if (updatedCount == 0) return null
    return get(id)
  }

  public suspend fun delete(id: LibraryBookId): Boolean {
    val deletedCount = suspendTransaction(database) {
      LibraryBookTable.deleteWhere { LibraryBookTable.id eq id.value }
    }
    return deletedCount > 0
  }
}
