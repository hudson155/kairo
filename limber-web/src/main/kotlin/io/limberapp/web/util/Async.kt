package io.limberapp.web.util

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

private val mainScope = MainScope()

internal fun async(block: suspend () -> Unit) {
    mainScope.launch { block() }
}
