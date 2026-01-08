package kairo.optional

import com.fasterxml.jackson.databind.module.SimpleModule

public class OptionalModule : SimpleModule() {
  init {
    addSerializer(Optional::class.java, OptionalSerializer())
    addDeserializer(Optional::class.java, OptionalDeserializer())
  }

  override fun setupModule(context: SetupContext) {
    super.setupModule(context)
    context.addTypeModifier(OptionalTypeModifier())
  }
}
