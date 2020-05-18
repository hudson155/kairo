package io.limberapp.web.util

import kotlinx.css.*
import kotlinx.css.properties.*
import styled.StyleSheet
import styled.getClassName
import kotlin.reflect.KProperty0

internal val gs = GlobalStyles().apply { inject() }

internal class GlobalStyles : Styles("Global") {
  val spinner by css {
    animation("spinner", duration = 1.2.s, timing = Timing.linear, iterationCount = IterationCount.infinite)
  }
  private val button by css {
    color = Theme.Color.Text.light
    fontSize = LinearDimension.initial
    fontWeight = FontWeight.bold
    border(1.px, BorderStyle.solid, Theme.Color.Border.light)
    borderRadius = 4.px
    padding(vertical = 6.px, horizontal = 12.px)
    cursor = Cursor.pointer
  }
  val primaryButton by css {
    +button
    backgroundColor = Theme.Color.Button.Primary.backgroundDefault
    hover {
      backgroundColor = Theme.Color.Button.Primary.backgroundActive
    }
    disabled {
      backgroundColor = Theme.Color.Button.Primary.backgroundDisabled
      hover {
        backgroundColor = Theme.Color.Button.Primary.backgroundDisabled
      }
    }
  }
  val secondaryButton by css {
    +button
    backgroundColor = Theme.Color.Button.Secondary.backgroundDefault
    hover {
      backgroundColor = Theme.Color.Button.Secondary.backgroundActive
    }
    disabled {
      backgroundColor = Theme.Color.Button.Secondary.backgroundDisabled
      hover {
        backgroundColor = Theme.Color.Button.Secondary.backgroundDisabled
      }
    }
  }
  val redButton by css {
    +button
    backgroundColor = Theme.Color.Button.Red.backgroundDefault
    hover {
      backgroundColor = Theme.Color.Button.Red.backgroundActive
    }
    disabled {
      backgroundColor = Theme.Color.Button.Red.backgroundDisabled
      hover {
        backgroundColor = Theme.Color.Button.Red.backgroundDisabled
      }
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

@Suppress("FunctionMinLength")
internal fun <T : StyleSheet> T.c(condition: Boolean, getClass: (T) -> KProperty0<RuleSet>) =
  if (condition) c(getClass) else null

@Suppress("FunctionMinLength")
internal fun <T : StyleSheet> T.c(getClass: (T) -> KProperty0<RuleSet>) = getClassName(getClass)

internal fun cls(vararg classes: String?) = classes.filterNotNull().joinToString(" ")
