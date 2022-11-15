package limber.type

import com.fasterxml.jackson.databind.ObjectMapper
import limber.serialization.ObjectMapperFactory

internal abstract class OptionalTest {
  protected val objectMapper: ObjectMapper = ObjectMapperFactory.builder(ObjectMapperFactory.Format.Json).build()

  abstract fun `equals method`()

  abstract fun `toString method`()

  abstract fun serialize()

  abstract fun deserialize()
}
