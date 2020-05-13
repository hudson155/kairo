package io.limberapp.web.util

import kotlinx.css.BorderStyle
import kotlinx.css.Cursor
import kotlinx.css.LinearDimension
import kotlinx.css.backgroundColor
import kotlinx.css.borderRadius
import kotlinx.css.color
import kotlinx.css.cursor
import kotlinx.css.fontSize
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
    val button by css {
        backgroundColor = Theme.Color.Button.Primary.backgroundDefault
        color = Theme.Color.Text.light
        fontSize = LinearDimension.initial
        border(1.px, BorderStyle.solid, Theme.Color.Border.light)
        borderRadius = 4.px
        padding(vertical = 4.px, horizontal = 8.px)
        cursor = Cursor.pointer
        hover {
            backgroundColor = Theme.Color.Button.Primary.backgroundActive
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
