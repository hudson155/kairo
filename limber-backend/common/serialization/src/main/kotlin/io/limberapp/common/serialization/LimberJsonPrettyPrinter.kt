package io.limberapp.common.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter

internal class LimberJsonPrettyPrinter : DefaultPrettyPrinter() {
  override fun createInstance() = LimberJsonPrettyPrinter()

  override fun writeObjectFieldValueSeparator(jg: JsonGenerator) {
    jg.writeRaw(_separators.objectFieldValueSeparator + " ")
  }
}
