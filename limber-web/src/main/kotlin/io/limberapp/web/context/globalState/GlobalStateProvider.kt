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
    org = LoadableState(
        LoadableState.LoadingStatus.LOADED, OrgRep.Complete(
            guid = "",
            createdDate = "",
            name = "",
            ownerAccountGuid = "",
            features = setOf(
                FeatureRep.Complete(
                    guid = "75a2ed7a-4247-4e63-ab10-a60df3d9aeee",
                    createdDate = Date().toISOString(),
                    name = "Home",
                    path = "/home",
                    type = FeatureRep.Type.HOME,
                    isDefaultFeature = true
                ),
                FeatureRep.Complete(
                    guid = "3dc95c5d-767c-4b29-9c50-a6f93edd0c06",
                    createdDate = Date().toISOString(),
                    name = "Forms",
                    path = "/forms",
                    type = FeatureRep.Type.FORMS,
                    isDefaultFeature = false
                )
            )
        )
    ),
    tenant = LoadableState.initial()
)

private val globalStateProvider = functionalComponent<RProps> { props ->
    val (state, dispatch) = useReducer({ state: GlobalStateContext, action: Action ->
        @Suppress("UseIfInsteadOfWhen")
        return@useReducer when (action) {
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
