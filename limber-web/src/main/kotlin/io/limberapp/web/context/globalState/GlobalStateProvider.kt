package io.limberapp.web.context.globalState

import io.limberapp.web.app.components.navbar.components.headerLink.Props
import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.ProviderValue
import io.limberapp.web.context.StateAndDispatch
import io.limberapp.web.context.globalState.action.Action
import io.limberapp.web.context.globalState.action.org.OrgAction
import io.limberapp.web.context.globalState.action.org.orgReducer
import io.limberapp.web.context.globalState.action.tenant.TenantAction
import io.limberapp.web.context.globalState.action.tenant.tenantReducer
import react.RBuilder
import react.RHandler
import react.RProps
import react.child
import react.children
import react.createContext
import react.createElement
import react.functionalComponent
import react.useContext
import react.useReducer

private val globalState = createContext<StateAndDispatch<GlobalStateContext, Action>>()
internal fun useGlobalState() = useContext(globalState)

private val initialState = GlobalStateContext(
    org = LoadableState.initial(),
    tenant = LoadableState.initial()
)

private val globalStateProvider = functionalComponent<RProps> { props ->
    val (state, dispatch) = useReducer({ state: GlobalStateContext, action: Action ->
        @Suppress("UseIfInsteadOfWhen")
        return@useReducer when (action) {
            is OrgAction -> orgReducer(state, action)
            is TenantAction -> tenantReducer(state, action)
            else -> error("Unhandled action: $action.")
        }
    }, initialState)
    val configObject = ProviderValue(StateAndDispatch(state, dispatch))
    child(createElement(globalState.Provider, configObject, props.children))
}

internal fun RBuilder.globalStateProvider(children: RHandler<Props>) {
    child(globalStateProvider, handler = children)
}
