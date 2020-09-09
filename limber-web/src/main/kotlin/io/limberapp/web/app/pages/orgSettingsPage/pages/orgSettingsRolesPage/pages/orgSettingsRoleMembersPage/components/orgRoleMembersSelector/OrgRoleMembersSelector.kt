package io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleMembersPage.components.orgRoleMembersSelector

import com.piperframework.types.UUID
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep
import io.limberapp.web.app.components.limberButton.Style
import io.limberapp.web.app.components.limberButton.limberButton
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleMembersPage.components.orgRoleMembersSelector.components.orgRoleMembersSelectorMember.orgRoleMembersSelectorMember
import io.limberapp.web.app.pages.orgSettingsPage.pages.orgSettingsRolesPage.pages.orgSettingsRoleMembersPage.components.orgRoleMembersSelector.components.orgRoleMembersSelectorMemberAdder.orgRoleMembersSelectorMemberAdder
import io.limberapp.web.state.state.orgRoleMemberships.useOrgRoleMembershipsState
import io.limberapp.web.state.state.users.useUsersState
import io.limberapp.web.util.async
import io.limberapp.web.util.c
import io.limberapp.web.util.gs
import react.*
import react.dom.*

/**
 * Selector for choosing a member from a list for an org role. Displays a list of all of the org role's members,
 * allowing for the addition of new members and the removal of existing ones. At the bottom there's an extra row which
 * is what lets the user add a new member.
 */
internal fun RBuilder.orgRoleMembersSelector(orgRoleGuid: UUID, onClose: () -> Unit) {
  child(component, Props(orgRoleGuid, onClose))
}

internal data class Props(val orgRoleGuid: UUID, val onClose: () -> Unit) : RProps

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val (orgRoleMemberships, orgRoleMembershipsMutator) = useOrgRoleMembershipsState()
  val (users, _) = useUsersState()

  val onAdd = { accountGuid: UUID ->
    async {
      orgRoleMembershipsMutator.post(props.orgRoleGuid, OrgRoleMembershipRep.Creation(accountGuid = accountGuid))
    }
  }

  val onRemove = { accountGuid: UUID ->
    async {
      orgRoleMembershipsMutator.delete(props.orgRoleGuid, accountGuid)
    }
  }

  val memberUsers = orgRoleMemberships
    .mapNotNull { users[it.key] }
    .sortedBy { it.fullName }

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
