package kairo.adminSample.libraryBook

import kotlin.time.Instant
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp

public object LibraryBookTable : Table("library_book") {
  public val id: Column<String> = text("id")
  public val createdAt: Column<Instant> = timestamp("created_at").defaultExpression(CurrentTimestamp)
  public val updatedAt: Column<Instant> = timestamp("updated_at").defaultExpression(CurrentTimestamp)
  public val title: Column<String> = text("title")
  public val isbn: Column<String> = text("isbn").uniqueIndex()
  public val genre: Column<String> = text("genre")

  override val primaryKey: PrimaryKey = PrimaryKey(id)

  public fun fromRow(row: ResultRow): LibraryBookModel =
    LibraryBookModel(
      id = LibraryBookId(row[id]),
      createdAt = row[createdAt],
      updatedAt = row[updatedAt],
      title = row[title],
      isbn = row[isbn],
      genre = row[genre],
    )
}
