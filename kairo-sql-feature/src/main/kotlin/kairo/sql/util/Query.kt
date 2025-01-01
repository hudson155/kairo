package kairo.sql.util

import org.jdbi.v3.core.qualifier.QualifiedType
import org.jdbi.v3.core.statement.Query
import org.jdbi.v3.json.Json

public inline fun <reified T : Any?> Query.bindJson(name: String, value: T): Query {
  val argumentType = QualifiedType.of(T::class.java).with(Json::class.java)
  return bindByType(name, value, argumentType)
}
