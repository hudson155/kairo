package io.limberapp.web.util

import io.limberapp.web.context.StateAndDispatch
import io.limberapp.web.context.api.Api
import io.limberapp.web.context.globalState.GlobalStateContext
import io.limberapp.web.context.globalState.action.Action

internal data class EnsureLoadedContext(val global: StateAndDispatch<GlobalStateContext, Action>, val api: Api)

internal fun <T> withContext(
  global: StateAndDispatch<GlobalStateContext, Action>,
  api: Api,
  function: EnsureLoadedContext.() -> T
): T {
  val context = EnsureLoadedContext(global, api)
  return context.function()
}
