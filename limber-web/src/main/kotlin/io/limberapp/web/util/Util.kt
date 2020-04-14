package io.limberapp.web.util

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.browser.window

internal val rootDomain = window.location.host
internal val rootUrl = "${window.location.protocol}//$rootDomain"

private val mainScope = MainScope()

internal fun async(block: suspend () -> Unit) {
    mainScope.launch { block() }
}
