package io.limberapp.web.util

import kotlinx.css.Cursor
import kotlinx.css.color
import kotlinx.css.cursor
import kotlinx.css.properties.IterationCount
import kotlinx.css.properties.TextDecorationLine
import kotlinx.css.properties.Timing
import kotlinx.css.properties.animation
import kotlinx.css.properties.s
import kotlinx.css.properties.textDecoration

internal val globalStyles = GlobalStyles().apply { inject() }

internal class GlobalStyles : Styles("Global") {
    val spinner by css {
        animation("spinner", duration = 1.2.s, timing = Timing.linear, iterationCount = IterationCount.infinite)
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
