package io.limberapp.web.context.globalState

import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.ProviderValue
import io.limberapp.web.context.StateAndDispatch
import io.limberapp.web.context.globalState.action.Action
import io.limberapp.web.context.globalState.action.formInstances.FormInstancesAction
import io.limberapp.web.context.globalState.action.formInstances.formInstancesReducer
import io.limberapp.web.context.globalState.action.formTemplates.FormTemplatesAction
import io.limberapp.web.context.globalState.action.formTemplates.formTemplatesReducer
import io.limberapp.web.context.globalState.action.org.OrgAction
import io.limberapp.web.context.globalState.action.org.orgReducer
import io.limberapp.web.context.globalState.action.orgRoleMemberships.OrgRoleMembershipsAction
import io.limberapp.web.context.globalState.action.orgRoleMemberships.orgRoleMembershipsReducer
import io.limberapp.web.context.globalState.action.orgRoles.OrgRolesAction
import io.limberapp.web.context.globalState.action.orgRoles.orgRolesReducer
import io.limberapp.web.context.globalState.action.tenant.TenantAction
import io.limberapp.web.context.globalState.action.tenant.tenantReducer
import io.limberapp.web.context.globalState.action.user.UserAction
import io.limberapp.web.context.globalState.action.user.userReducer
import io.limberapp.web.context.globalState.action.users.UsersAction
import io.limberapp.web.context.globalState.action.users.usersReducer
import react.*

/**
 * Provides global state, like Redux.
 */
internal fun RBuilder.globalStateProvider(children: RHandler<RProps>) {
  child(component, handler = children)
}

private val globalState = createContext<StateAndDispatch<GlobalStateContext, Action>>()
internal fun useGlobalState() = useContext(globalState)

private val initialState = GlobalStateContext(
  formInstances = emptyMap(),
  formTemplates = emptyMap(),
  org = LoadableState.initial(),
  orgRoleMemberships = emptyMap(),
  orgRoles = LoadableState.initial(),
  tenant = LoadableState.initial(),
  user = LoadableState.initial(),
  users = LoadableState.initial()
)

private val component = functionalComponent<RProps> { props ->
  val (state, dispatch) = useReducer({ state: GlobalStateContext, action: Action ->
    return@useReducer when (action) {
      is FormInstancesAction -> state.copy(formInstances = formInstancesReducer(state.formInstances, action))
      is FormTemplatesAction -> state.copy(formTemplates = formTemplatesReducer(state.formTemplates, action))
      is OrgAction -> state.copy(org = orgReducer(state.org, action))
      is OrgRoleMembershipsAction -> state.copy(
        orgRoleMemberships = orgRoleMembershipsReducer(state.orgRoleMemberships, action)
      )
      is OrgRolesAction -> state.copy(orgRoles = orgRolesReducer(state.orgRoles, action))
      is TenantAction -> state.copy(tenant = tenantReducer(state.tenant, action))
      is UserAction -> state.copy(user = userReducer(state.user, action))
      is UsersAction -> state.copy(users = usersReducer(state.users, action))
      else -> error("Unhandled action: $action.")
    }
  }, initialState)
  val configObject = ProviderValue(StateAndDispatch(state, dispatch))
  child(createElement(globalState.Provider, configObject, props.children))
}
