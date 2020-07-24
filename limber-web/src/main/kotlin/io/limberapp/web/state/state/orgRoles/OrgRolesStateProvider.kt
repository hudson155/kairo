package io.limberapp.web.state.state.orgRoles

import com.piperframework.types.UUID
import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.api.useApi
import io.limberapp.web.state.ProviderValue
import io.limberapp.web.state.state.orgState.useOrgState
import react.*

internal fun RBuilder.orgRolesStateProvider(orgRoles: OrgRolesState, children: RHandler<Props>) {
  child(component, Props(orgRoles), handler = children)
}

internal data class Props(val orgRoles: OrgRolesState) : RProps

private val orgRoles = createContext<Pair<OrgRolesState, OrgRolesMutator>>()
internal fun useOrgRolesState() = useContext(orgRoles)

private val component = functionalComponent(RBuilder::component)
private fun RBuilder.component(props: Props) {
  val api = useApi()

  val (org, _) = useOrgState()

  val (state, setState) = useState(props.orgRoles)

  val mutator = object : OrgRolesMutator {
    override suspend fun post(rep: OrgRoleRep.Creation) =
      api(OrgRoleApi.Post(org.guid, rep)).map { orgRole ->
        setState(state + (orgRole.guid to orgRole))
      }

    override suspend fun patch(orgRoleGuid: UUID, rep: OrgRoleRep.Update) =
      api(OrgRoleApi.Patch(org.guid, orgRoleGuid, rep)).map { orgRole ->
        setState(state + (orgRole.guid to orgRole))
      }

    override fun localMemberCountChanged(orgRoleGuid: UUID, by: Int) {
      val orgRole = state[orgRoleGuid] ?: return // Fail silently.
      setState(state + (orgRoleGuid to orgRole.copy(memberCount = orgRole.memberCount + by)))
    }

    override suspend fun delete(orgRoleGuid: UUID) =
      api(OrgRoleApi.Delete(org.guid, orgRoleGuid)).map {
        setState(state - (orgRoleGuid))
      }
  }

  val configObject = ProviderValue(Pair(state, mutator))
  child(createElement(orgRoles.Provider, configObject, props.children))
}
