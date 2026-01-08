package kairo.optional

import com.fasterxml.jackson.databind.module.SimpleModule

public class OptionalModule : SimpleModule() {
  init {
    addSerializer(Optional::class.java, OptionalSerializer())
    addDeserializer(Optional::class.java, OptionalDeserializer())

    addSerializer(Required::class.java, RequiredSerializer())
    addDeserializer(Required::class.java, RequiredDeserializer())
  }

  override fun setupModule(context: SetupContext) {
    super.setupModule(context)
    context.addTypeModifier(OptionalTypeModifier())
    context.addTypeModifier(RequiredTypeModifier())
  }
}
