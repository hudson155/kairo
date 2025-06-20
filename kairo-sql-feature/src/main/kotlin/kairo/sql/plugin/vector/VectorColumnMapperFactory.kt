package kairo.sql.plugin.vector

import java.util.Optional
import org.jdbi.v3.core.config.ConfigRegistry
import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.mapper.QualifiedColumnMapperFactory
import org.jdbi.v3.core.qualifier.QualifiedType

internal class VectorColumnMapperFactory : QualifiedColumnMapperFactory {
  override fun build(qualifiedType: QualifiedType<*>, config: ConfigRegistry): Optional<ColumnMapper<*>> {
    if (!qualifiedType.qualifiers.any { it.annotationClass == Vector::class }) return Optional.empty()
    val columnMapper = ColumnMapper { r, columnNumber, ctx ->
      val string = r.getString(columnNumber)
      return@ColumnMapper string.removeSurrounding("[", "]").split(",").map { it.toFloat() }
    }
    return Optional.of(columnMapper)
  }
}
