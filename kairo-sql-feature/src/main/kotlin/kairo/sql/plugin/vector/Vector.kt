package kairo.sql.plugin.vector

import org.jdbi.v3.core.qualifier.Qualifier

@Target(
  AnnotationTarget.FIELD,
  AnnotationTarget.FUNCTION,
  AnnotationTarget.VALUE_PARAMETER,
  AnnotationTarget.TYPE,
)
@Qualifier
public annotation class Vector
