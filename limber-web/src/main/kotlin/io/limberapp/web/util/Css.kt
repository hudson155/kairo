package io.limberapp.web.util

import kotlinx.css.BorderStyle
import kotlinx.css.Cursor
import kotlinx.css.FontWeight
import kotlinx.css.LinearDimension
import kotlinx.css.backgroundColor
import kotlinx.css.borderRadius
import kotlinx.css.color
import kotlinx.css.cursor
import kotlinx.css.fontSize
import kotlinx.css.fontWeight
import kotlinx.css.padding
import kotlinx.css.properties.IterationCount
import kotlinx.css.properties.TextDecorationLine
import kotlinx.css.properties.Timing
import kotlinx.css.properties.animation
import kotlinx.css.properties.border
import kotlinx.css.properties.s
import kotlinx.css.properties.textDecoration
import kotlinx.css.px

internal val globalStyles = GlobalStyles().apply { inject() }

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

internal fun classes(vararg classes: String?) = classes.filterNotNull().joinToString(" ")
