package kairo.serialization

import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.core.util.Separators
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.cfg.JsonNodeFeature

internal fun ObjectMapperFactoryBuilder.configurePrettyPrinting() {
  configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false)
  configure(MapperFeature.SORT_CREATOR_PROPERTIES_FIRST, false)

  configure(SerializationFeature.INDENT_OUTPUT, false)
  configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, false)

  configure(JsonNodeFeature.WRITE_PROPERTIES_SORTED, false)

  defaultPrettyPrinter(
    DefaultPrettyPrinter()
      .withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE)
      .withSeparators(
        Separators.createDefaultInstance()
          .withObjectFieldValueSpacing(Separators.Spacing.AFTER),
      ),
  )
}
