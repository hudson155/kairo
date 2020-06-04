package io.limberapp.web.util

import io.limberapp.web.context.api.useApi
import io.limberapp.web.context.globalState.useGlobalState
import react.*

internal open class Component

internal open class ComponentWithGlobalState : Component() {
  private val global = useGlobalState()
  val gs = global.state
  val dispatch = global.dispatch
}

internal open class ComponentWithApi : ComponentWithGlobalState() {
  val api = useApi()
}

internal fun <P : RProps> component(
  function: RBuilder.(props: P) -> Unit
): FunctionalComponent<P> = functionalComponent { function(it) }

internal fun <P : RProps> componentWithGlobalState(
  function: RBuilder.(self: ComponentWithGlobalState, props: P) -> Unit
): FunctionalComponent<P> = functionalComponent { function(ComponentWithGlobalState(), it) }

internal fun <P : RProps> componentWithApi(
  function: RBuilder.(self: ComponentWithApi, props: P) -> Unit
): FunctionalComponent<P> = functionalComponent { function(ComponentWithApi(), it) }
