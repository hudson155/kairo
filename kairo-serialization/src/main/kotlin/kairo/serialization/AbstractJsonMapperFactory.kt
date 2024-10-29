package kairo.serialization

import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.core.util.Separators
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.cfg.MapperBuilder
import kairo.serialization.module.primitives.DefaultPrimitivesModule

public abstract class AbstractJsonMapperFactory<M : ObjectMapper, B : MapperBuilder<M, B>> internal constructor() :
  ObjectMapperFactory<M, B>() {
  override fun configurePrimitives(builder: B) {
    builder.addModule(DefaultPrimitivesModule.from(this))
  }

  override fun configurePrettyPrinting(builder: B) {
    super.configurePrettyPrinting(builder)
    builder.defaultPrettyPrinter(
      DefaultPrettyPrinter()
        .withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE)
        .withSeparators(
          Separators.createDefaultInstance()
            .withObjectFieldValueSpacing(Separators.Spacing.AFTER),
        ),
    )
  }
}
