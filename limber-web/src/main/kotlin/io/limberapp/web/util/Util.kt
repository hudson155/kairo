package io.limberapp.web.util

import io.limberapp.web.context.StateAndDispatch
import io.limberapp.web.context.api.Api
import io.limberapp.web.context.globalState.GlobalStateContext
import io.limberapp.web.context.globalState.action.Action
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

internal data class EnsureLoadedContext(val global: StateAndDispatch<GlobalStateContext, Action>, val api: Api)

internal fun <T> withContext(
    global: StateAndDispatch<GlobalStateContext, Action>,
    api: Api,
    function: EnsureLoadedContext.() -> T
): T {
    val context = EnsureLoadedContext(global, api)
    return context.function()
}

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
internal fun buildElements(handler: RBuilder.() -> Unit) = react.buildElements(handler) as ReactElement?
