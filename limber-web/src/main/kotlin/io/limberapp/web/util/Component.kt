package io.limberapp.web.util

import io.limberapp.web.api.useApi
import io.limberapp.web.context.globalState.useGlobalState
import react.*

/**
 * Many components will use this class. It should be the default unless behaviours for more advanced components are
 * required.
 */
internal open class Component

/**
 * If a component needs to access the global state it should do so by using this class.
 */
internal open class ComponentWithGlobalState : Component() {
  private val global = useGlobalState()
  val gs = global.state
  val dispatch = global.dispatch
}

/**
 * If a component needs to access the API it should do so by using this class.
 */
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
