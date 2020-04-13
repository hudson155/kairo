package io.limberapp.web.context

internal data class StateAndDispatch<State : Any, Action : Any>(val state: State, val dispatch: (Action) -> Unit)
