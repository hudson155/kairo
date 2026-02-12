package kairo.adminSample.author

import kotlin.time.Instant
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentTimestamp
import org.jetbrains.exposed.v1.datetime.timestamp

public object AuthorTable : Table("author") {
  public val id: Column<String> = text("id")
  public val createdAt: Column<Instant> = timestamp("created_at").defaultExpression(CurrentTimestamp)
  public val updatedAt: Column<Instant> = timestamp("updated_at").defaultExpression(CurrentTimestamp)
  public val name: Column<String> = text("name")
  public val bio: Column<String?> = text("bio").nullable()

  override val primaryKey: PrimaryKey = PrimaryKey(id)

  public fun fromRow(row: ResultRow): AuthorModel =
    AuthorModel(
      id = AuthorId(row[id]),
      createdAt = row[createdAt],
      updatedAt = row[updatedAt],
      name = row[name],
      bio = row[bio],
    )
}
