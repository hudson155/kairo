package io.limberapp.web.state.state.orgRoleMemberships

import com.piperframework.types.UUID
import io.limberapp.backend.module.auth.api.org.role.OrgRoleMembershipApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep
import io.limberapp.web.api.useApi
import io.limberapp.web.state.ProviderValue
import io.limberapp.web.state.state.orgRoles.useOrgRolesState
import io.limberapp.web.state.state.orgState.useOrgState
import react.*

internal fun RBuilder.orgRoleMembershipsStateProvider(
  orgRoleMemberships: OrgRoleMembershipsState,
  children: RHandler<Props>
) {
  child(component, Props(orgRoleMemberships), handler = children)
}

internal data class Props(val orgRoleMemberships: OrgRoleMembershipsState) : RProps

private val orgRoleMemberships = createContext<Pair<OrgRoleMembershipsState, OrgRoleMembershipsMutator>>()
internal fun useOrgRoleMembershipsState() = useContext(orgRoleMemberships)

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val api = useApi()

  val (org, _) = useOrgState()
  val (_, orgRolesMutator) = useOrgRolesState()

  val (state, setState) = useState(props.orgRoleMemberships)

  val mutator = object : OrgRoleMembershipsMutator {
    override suspend fun post(orgRoleGuid: UUID, rep: OrgRoleMembershipRep.Creation) =
      api(OrgRoleMembershipApi.Post(org.guid, orgRoleGuid, rep)).map { orgRoleMembership ->
        orgRolesMutator.localMemberCountChanged(orgRoleGuid, by = 1)
        setState(state + (rep.accountGuid to orgRoleMembership))
      }

    override suspend fun delete(orgRoleGuid: UUID, accountGuid: UUID) =
      api(OrgRoleMembershipApi.Delete(org.guid, orgRoleGuid, accountGuid)).map {
        orgRolesMutator.localMemberCountChanged(orgRoleGuid, by = -1)
        setState(state - accountGuid)
      }
  }

  val configObject = ProviderValue(Pair(state, mutator))
  child(createElement(orgRoleMemberships.Provider, configObject, props.children))
}
