package limber.serialization

import com.fasterxml.jackson.databind.module.SimpleModule

/**
 * See [TrimWhitespace].
 * By default, both the start and end will be trimmed.
 */
public object StringTrimModule : SimpleModule() {
  init {
    addDeserializer(String::class.java, LimberStringDeserializer(TrimWhitespace.Type.TrimBoth))
  }
}
