package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage.components.orgRoleMembersSelector

import com.piperframework.types.UUID
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.app.components.limberButton.Style
import io.limberapp.web.app.components.limberButton.limberButton
import io.limberapp.web.app.components.loadingSpinner.loadingSpinner
import io.limberapp.web.app.pages.failedToLoad.failedToLoad
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage.components.orgRoleMembersSelector.components.orgRoleMembersSelectorMember.orgRoleMembersSelectorMember
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleDetailPage.components.orgRoleMembersSelector.components.orgRoleMembersSelectorMemberAdder.orgRoleMembersSelectorMemberAdder
import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.globalState.action.orgRoleMemberships.createOrgRoleMembership
import io.limberapp.web.context.globalState.action.orgRoleMemberships.deleteOrgRoleMembership
import io.limberapp.web.context.globalState.action.orgRoleMemberships.loadOrgRoleMemberships
import io.limberapp.web.context.globalState.action.users.loadUsers
import io.limberapp.web.util.async
import io.limberapp.web.util.c
import io.limberapp.web.util.componentWithApi
import io.limberapp.web.util.gs
import react.*
import react.dom.*

/**
 * Selector for choosing a member from a list for an org role. Displays a list of all of the org role's members,
 * allowing for the addition of new members and the removal of existing ones. At the bottom there's an extra row which
 * is what lets the user add a new member.
 */
internal fun RBuilder.orgRoleMembersSelector(orgRole: OrgRoleRep.Complete, onClose: () -> Unit) {
  child(component, Props(orgRole, onClose))
}

internal data class Props(val orgRole: OrgRoleRep.Complete, val onClose: () -> Unit) : RProps

private val component = componentWithApi<Props> component@{ self, props ->
  self.loadUsers()
  self.loadOrgRoleMemberships(props.orgRole.guid)

  val onAdd = { accountGuid: UUID ->
    async { self.createOrgRoleMembership(props.orgRole.guid, OrgRoleMembershipRep.Creation(accountGuid = accountGuid)) }
  }

  val onRemove = { accountGuid: UUID ->
    async { self.deleteOrgRoleMembership(props.orgRole.guid, accountGuid) }
  }

  // While the users are loading, show a loading spinner.
  val users = self.gs.users.let { loadableState ->
    when (loadableState) {
      is LoadableState.Unloaded -> return@component loadingSpinner()
      is LoadableState.Error -> return@component failedToLoad("users")
      is LoadableState.Loaded -> return@let loadableState.state
    }
  }

  // While the users are loading, show a loading spinner.
  val orgRoleMemberships = self.gs.orgRoleMemberships[props.orgRole.guid].let { loadableState ->
    when (loadableState) {
      null, is LoadableState.Unloaded -> return@component loadingSpinner()
      is LoadableState.Error -> return@component failedToLoad("roles")
      is LoadableState.Loaded -> return@let loadableState.state
    }
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
  div(classes = gs.c { it::modalFooter }) {
    limberButton(
      style = Style.PRIMARY,
      onClick = props.onClose
    ) { +"Done" }
  }
}
