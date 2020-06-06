package io.limberapp.web.context.globalState.action.orgRoleMemberships

import com.piperframework.types.UUID
import io.limberapp.backend.module.auth.api.org.role.OrgRoleMembershipApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep
import io.limberapp.web.context.globalState.action.Action
import io.limberapp.web.context.globalState.action.org.state
import io.limberapp.web.util.ComponentWithApi
import io.limberapp.web.util.async
import react.*

internal sealed class OrgRoleMembershipsAction : Action() {
  internal data class BeginLoading(val orgRoleGuid: UUID) : OrgRoleMembershipsAction()

  internal data class SetValue(
    val orgRoleGuid: UUID,
    val orgRoleMemberships: Set<OrgRoleMembershipRep.Complete>
  ) : OrgRoleMembershipsAction()

  internal data class UpdateValue(
    val orgRoleGuid: UUID,
    val orgRoleMembership: OrgRoleMembershipRep.Complete
  ) : OrgRoleMembershipsAction()

  internal data class DeleteValue(
    val orgRoleGuid: UUID,
    val accountGuid: UUID
  ) : OrgRoleMembershipsAction()

  internal data class SetError(
    val orgRoleGuid: UUID,
    val errorMessage: String?
  ) : OrgRoleMembershipsAction()
}

internal fun ComponentWithApi.loadOrgRoleMemberships(orgRoleGuid: UUID) {
  val orgGuid = gs.org.state.guid

  useEffect(listOf(orgRoleGuid)) {
    if (gs.orgRoleMemberships[orgRoleGuid]?.hasBegunLoading == true) return@useEffect
    dispatch(OrgRoleMembershipsAction.BeginLoading(orgRoleGuid))
    async {
      api.orgRoleMemberships(OrgRoleMembershipApi.GetByOrgRoleGuid(orgGuid, orgRoleGuid)).fold(
        onSuccess = { orgRoleMemberships ->
          dispatch(OrgRoleMembershipsAction.SetValue(orgRoleGuid, orgRoleMemberships))
        },
        onFailure = {
          dispatch(OrgRoleMembershipsAction.SetError(orgRoleGuid, it.message))
        }
      )
    }
  }
}

internal suspend fun ComponentWithApi.createOrgRoleMembership(orgRoleGuid: UUID, rep: OrgRoleMembershipRep.Creation) {
  api.orgRoleMemberships(
    endpoint = OrgRoleMembershipApi.Post(
      orgGuid = gs.org.state.guid,
      orgRoleGuid = orgRoleGuid,
      rep = rep
    )
  ).fold(
    onSuccess = { orgRoleMembership -> dispatch(OrgRoleMembershipsAction.UpdateValue(orgRoleGuid, orgRoleMembership)) },
    onFailure = { dispatch(OrgRoleMembershipsAction.SetError(orgRoleGuid, it.message)) }
  )
}

internal suspend fun ComponentWithApi.deleteOrgRoleMembership(orgRoleGuid: UUID, accountGuid: UUID) {
  api.orgRoleMemberships(
    endpoint = OrgRoleMembershipApi.Delete(
      orgGuid = gs.org.state.guid,
      orgRoleGuid = orgRoleGuid,
      accountGuid = accountGuid
    )
  ).fold(
    onSuccess = { dispatch(OrgRoleMembershipsAction.DeleteValue(orgRoleGuid, accountGuid)) },
    onFailure = { dispatch(OrgRoleMembershipsAction.SetError(orgRoleGuid, it.message)) }
  )
}
