package kairo.serialization.module

import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.core.util.Separators
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.cfg.JsonNodeFeature
import kairo.serialization.ObjectMapperFactoryBuilder

/**
 * See the corresponding test for more spec.
 */
internal fun ObjectMapperFactoryBuilder.configurePrettyPrinting(prettyPrint: Boolean) {
  configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, prettyPrint)
  configure(MapperFeature.SORT_CREATOR_PROPERTIES_FIRST, false)

  configure(SerializationFeature.INDENT_OUTPUT, prettyPrint)
  configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, prettyPrint)

  configure(JsonNodeFeature.WRITE_PROPERTIES_SORTED, prettyPrint)

  defaultPrettyPrinter(
    DefaultPrettyPrinter()
      .withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE)
      .withSeparators(
        Separators.createDefaultInstance()
          .withObjectFieldValueSpacing(Separators.Spacing.AFTER),
      ),
  )
}
