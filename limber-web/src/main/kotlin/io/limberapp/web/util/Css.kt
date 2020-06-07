@file:Suppress("FunctionMinLength")

package io.limberapp.web.util

import kotlinx.css.*
import kotlinx.css.properties.*
import styled.StyleSheet
import styled.getClassName
import kotlin.reflect.KProperty0

internal val gs = GlobalStyles().apply { inject() }

internal fun CSSBuilder.xs(block: RuleSet) = media("(max-width: 767px)", block)

internal fun CSSBuilder.notXs(block: RuleSet) = media("(min-width: 768px)", block)

internal class GlobalStyles : Styles("Global") {
  val hiddenXs by css {
    xs {
      display = Display.none
    }
  }
  val visibleXs by css {
    notXs {
      display = Display.none
    }
  }
  val link by css {
    color = Theme.Color.Text.link
    cursor = Cursor.pointer
    hover {
      textDecoration(TextDecorationLine.underline)
    }
  }
}

internal fun <T : StyleSheet> T.c(condition: Boolean, getClass: (T) -> KProperty0<RuleSet>) =
  if (condition) c(getClass) else null

internal fun <T : StyleSheet> T.c(getClass: (T) -> KProperty0<RuleSet>) = getClassName(getClass)

internal fun cls(vararg classes: String?) = classes.filterNotNull().joinToString(" ")
