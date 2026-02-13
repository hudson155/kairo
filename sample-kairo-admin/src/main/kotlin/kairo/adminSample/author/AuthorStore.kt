package kairo.adminSample.author

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

public class AuthorStore(
  private val database: R2dbcDatabase,
) {
  public suspend fun get(id: AuthorId): AuthorModel? =
    suspendTransaction(database) {
      AuthorTable
        .selectAll()
        .where { AuthorTable.id eq id.value }
        .map { AuthorTable.fromRow(it) }
        .singleOrNull()
    }

  public suspend fun listAll(): List<AuthorModel> =
    suspendTransaction(database) {
      AuthorTable
        .selectAll()
        .map { AuthorTable.fromRow(it) }
        .toList()
    }

  public suspend fun create(creator: AuthorModel.Creator): AuthorModel {
    val authorId = AuthorId.random()
    suspendTransaction(database) {
      AuthorTable.insert { statement ->
        statement[id] = authorId.value
        statement[name] = creator.name
        statement[bio] = creator.bio
      }
    }
    return get(authorId) ?: error("Failed to create author.")
  }

  public suspend fun update(id: AuthorId, update: AuthorModel.Update): AuthorModel? {
    val now = Clock.System.now()
    val updatedCount = suspendTransaction(database) {
      AuthorTable.update({ AuthorTable.id eq id.value }) { statement ->
        update.name?.let { value -> statement[name] = value }
        update.bio?.let { value -> statement[bio] = value }
        statement[updatedAt] = now
      }
    }
    if (updatedCount == 0) return null
    return get(id)
  }

  public suspend fun delete(id: AuthorId): Boolean {
    val deletedCount = suspendTransaction(database) {
      AuthorTable.deleteWhere { AuthorTable.id eq id.value }
    }
    return deletedCount > 0
  }
}
