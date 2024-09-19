package kairo.serialization.module.java

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import java.util.Optional
import kairo.serialization.ObjectMapperFactoryBuilder

/**
 * The only thing we need to configure is support for [Optional]s,
 * which is not included in Jackson's core until 3.0.
 *
 * See the corresponding test for more spec.
 */
internal fun ObjectMapperFactoryBuilder.configureJava() {
  addModule(
    Jdk8Module().apply {
      configureReadAbsentAsNull(true)
    },
  )
}
