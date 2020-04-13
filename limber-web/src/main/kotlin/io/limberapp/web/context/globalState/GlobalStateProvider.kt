package io.limberapp.web.context.globalState

import io.limberapp.backend.module.orgs.rep.org.FeatureRep
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.web.app.components.navbar.components.headerLink.Props
import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.ProviderValue
import io.limberapp.web.context.StateAndDispatch
import io.limberapp.web.context.globalState.action.Action
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
import kotlin.js.Date

private val globalState = createContext<StateAndDispatch<GlobalStateContext, Action>>()
internal fun useGlobalState() = useContext(globalState)

private val initialState = GlobalStateContext(
    message = "testing message here!",
    tenant = LoadableState.initial(),
    org = LoadableState(
        LoadableState.LoadingStatus.LOADED, OrgRep.Complete(
            id = "",
            created = "",
            name = "",
            features = setOf(
                FeatureRep.Complete(
                    id = "75a2ed7a-4247-4e63-ab10-a60df3d9aeee",
                    created = Date().toISOString(),
                    name = "Home",
                    path = "/home",
                    type = FeatureRep.Type.HOME,
                    isDefaultFeature = true
                ),
                FeatureRep.Complete(
                    id = "3dc95c5d-767c-4b29-9c50-a6f93edd0c06",
                    created = Date().toISOString(),
                    name = "Forms",
                    path = "/forms",
                    type = FeatureRep.Type.FORMS,
                    isDefaultFeature = false
                )
            )
        )
    )
)

private val stateProvider = functionalComponent<RProps> { props ->
    val (state, dispatch) = useReducer({ state: GlobalStateContext, action: Action ->
        return@useReducer when (action) {
            is TenantAction -> tenantReducer(state, action)
            else -> throw Exception("Unhandled action: $action.")
        }
    }, initialState)
    val configObject = ProviderValue(StateAndDispatch(state, dispatch))
    child(createElement(globalState.Provider, configObject, props.children))
}

internal fun RBuilder.stateProvider(children: RHandler<Props>) {
    child(stateProvider, handler = children)
}
