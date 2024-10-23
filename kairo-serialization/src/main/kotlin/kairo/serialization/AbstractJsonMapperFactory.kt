package kairo.serialization

import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.core.util.Separators
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.cfg.MapperBuilder

public abstract class AbstractJsonMapperFactory<M : ObjectMapper, B : MapperBuilder<M, B>> internal constructor(
  modules: List<Module>,
) : ObjectMapperFactory<M, B>(modules) {
  override fun B.configurePrettyPrinting() {
    defaultPrettyPrinter(
      DefaultPrettyPrinter()
        .withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE)
        .withSeparators(
          Separators.createDefaultInstance()
            .withObjectFieldValueSpacing(Separators.Spacing.AFTER),
        ),
    )
  }
}
