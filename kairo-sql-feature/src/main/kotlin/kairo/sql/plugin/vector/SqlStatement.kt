package kairo.sql.plugin.vector

import kairo.reflect.kairoType
import org.jdbi.v3.core.qualifier.QualifiedType
import org.jdbi.v3.core.statement.SqlStatement

public fun <T : SqlStatement<T>> SqlStatement<T>.bindVector(name: String, value: List<Float>): T =
  bindByType(name, value, QualifiedType.of(kairoType<List<Float>>().javaType).with(Vector::class.java))
