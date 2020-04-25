package io.limberapp.web.util

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.RBuilder
import react.ReactElement
import kotlin.browser.window

internal val rootDomain = window.location.host
internal val rootUrl = "${window.location.protocol}//$rootDomain"

private val mainScope = MainScope()

internal fun async(block: suspend () -> Unit) {
    mainScope.launch { block() }
}

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
internal fun buildElements(handler: RBuilder.() -> Unit) = react.buildElements(handler) as ReactElement?
