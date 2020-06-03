package io.limberapp.web.util

import io.limberapp.web.context.StateAndDispatch
import io.limberapp.web.context.api.Api
import io.limberapp.web.context.globalState.GlobalStateContext
import io.limberapp.web.context.globalState.action.Action

internal data class Context(val global: StateAndDispatch<GlobalStateContext, Action>, val api: Api)

internal suspend fun <T> withContextAsync(
  global: StateAndDispatch<GlobalStateContext, Action>,
  api: Api,
  function: suspend Context.() -> T
): T {
  val context = Context(global, api)
  return context.function()
}

internal fun <T> withContext(
  global: StateAndDispatch<GlobalStateContext, Action>,
  api: Api,
  function: Context.() -> T
): T {
  val context = Context(global, api)
  return context.function()
}
