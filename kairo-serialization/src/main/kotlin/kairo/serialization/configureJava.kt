package kairo.serialization

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module

internal fun ObjectMapperFactoryBuilder.configureJava() {
  addModule(
    Jdk8Module().apply {
      configureReadAbsentAsNull(true)
    },
  )
}
