package limber.serialization

import com.fasterxml.jackson.databind.module.SimpleModule

public object StringTrimModule : SimpleModule() {
  init {
    addDeserializer(String::class.java, LimberStringDeserializer(TrimWhitespace.Type.TrimBoth))
  }
}
