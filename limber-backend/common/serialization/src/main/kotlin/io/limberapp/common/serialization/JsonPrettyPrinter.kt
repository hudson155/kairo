package io.limberapp.common.serialization

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter

internal class JsonPrettyPrinter : DefaultPrettyPrinter() {
  override fun createInstance(): JsonPrettyPrinter = JsonPrettyPrinter()

  override fun writeObjectFieldValueSeparator(jg: JsonGenerator) {
    jg.writeRaw(_separators.objectFieldValueSeparator + " ")
  }
}
