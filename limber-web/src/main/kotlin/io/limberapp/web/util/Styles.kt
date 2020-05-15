package io.limberapp.web.util

import styled.StyleSheet

internal abstract class Styles(name: String) : StyleSheet(name, isStatic = true) {
  init {
    check(name !in styleSheetNames) { "Duplicate style sheet name: $name." }
    styleSheetNames += name
  }

  companion object {
    val styleSheetNames = mutableSetOf<String>()
  }
}
