package io.limberapp.web.util

import kotlinx.css.properties.IterationCount
import kotlinx.css.properties.Timing
import kotlinx.css.properties.animation
import kotlinx.css.properties.s

internal val globalStyles = GlobalStyles().apply { inject() }

internal class GlobalStyles : Styles("Global") {
    val spinner by css {
        animation("spinner", duration = 1.2.s, timing = Timing.linear, iterationCount = IterationCount.infinite)
    }
}

internal fun classes(vararg classes: String?) = classes.filterNotNull().joinToString(" ")
