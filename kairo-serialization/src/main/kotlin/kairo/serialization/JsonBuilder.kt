package kairo.serialization

import kotlinx.serialization.json.JsonBuilder

public fun JsonBuilder.prettyPrint() {
  prettyPrint = true
  prettyPrintIndent = "  "
}
