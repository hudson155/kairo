package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage.components.orgRoleMembersSelector

import com.piperframework.types.UUID
import io.limberapp.backend.module.auth.api.org.role.OrgRoleMembershipApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.app.components.loadingSpinner.loadingSpinner
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage.components.orgRoleMembersSelector.components.orgRoleMembersSelectorMember.orgRoleMembersSelectorMember
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage.components.orgRoleMembersSelector.components.orgRoleMembersSelectorMemberAdder.orgRoleMembersSelectorMemberAdder
import io.limberapp.web.context.api.useApi
import io.limberapp.web.context.globalState.action.orgRoleMembership.OrgRoleMembershipAction
import io.limberapp.web.context.globalState.action.orgRoleMembership.ensureOrgRoleMembershipsLoaded
import io.limberapp.web.context.globalState.action.users.ensureUsersLoaded
import io.limberapp.web.context.globalState.useGlobalState
import io.limberapp.web.util.async
import io.limberapp.web.util.withContext
import react.*

/**
 * Selector for choosing a member from a list for an org role. Displays a list of all of the org role's members,
 * allowing for the addition of new members and the removal of existing ones.
 */
internal fun RBuilder.orgRoleMembersSelector(orgRole: OrgRoleRep.Complete) {
  child(component, Props(orgRole))
}

internal data class Props(val orgRole: OrgRoleRep.Complete) : RProps

private val component = functionalComponent<Props> { props ->
  val api = useApi()
  val global = useGlobalState()

  val orgGuid = checkNotNull(global.state.org.state).guid

  val onAdd = { accountGuid: UUID ->
    async {
      val orgRoleMembership = api.orgRoleMemberships(
        endpoint = OrgRoleMembershipApi.Post(
          orgGuid = orgGuid,
          orgRoleGuid = props.orgRole.guid,
          rep = OrgRoleMembershipRep.Creation(accountGuid = accountGuid)
        )
      )
      global.dispatch(OrgRoleMembershipAction.UpdateValue(props.orgRole.guid, orgRoleMembership))
    }
  }

  val onRemove = { accountGuid: UUID ->
    async {
      api.orgRoleMemberships(
        endpoint = OrgRoleMembershipApi.Delete(
          orgGuid = orgGuid,
          orgRoleGuid = props.orgRole.guid,
          accountGuid = accountGuid
        )
      )
      global.dispatch(OrgRoleMembershipAction.DeleteValue(props.orgRole.guid, accountGuid))
    }
  }

  withContext(global, api) {
    ensureUsersLoaded(checkNotNull(global.state.org.state).guid)
  }

  withContext(global, api) {
    ensureOrgRoleMembershipsLoaded(checkNotNull(global.state.org.state).guid, props.orgRole.guid)
  }

  // While the users are loading, show a loading spinner.
  val users = global.state.users.let { loadableState ->
    if (!loadableState.isLoaded) return@functionalComponent loadingSpinner()
    return@let checkNotNull(loadableState.state)
  }

  // While the users are loading, show a loading spinner.
  val orgRoleMemberships = global.state.orgRoleMemberships[props.orgRole.guid].let { loadableState ->
    if (loadableState?.isLoaded != true) return@functionalComponent loadingSpinner()
    return@let checkNotNull(loadableState.state)
  }

  val memberUsers = orgRoleMemberships
    .mapNotNull { users[it.key] }
    .sortedBy { it.uniqueSortKey }

  memberUsers.forEach { user ->
    orgRoleMembersSelectorMember(user, onRemove = { onRemove(user.guid) }) {
      key = user.guid
    }
  }
  orgRoleMembersSelectorMemberAdder(excludedUserGuids = orgRoleMemberships.keys, onAdd = onAdd)
}
