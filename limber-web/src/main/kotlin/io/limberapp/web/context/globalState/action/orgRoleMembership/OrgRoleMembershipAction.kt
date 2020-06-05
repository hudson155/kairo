package io.limberapp.web.context.globalState.action.orgRoleMembership

import com.piperframework.types.UUID
import io.limberapp.backend.module.auth.api.org.role.OrgRoleMembershipApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep
import io.limberapp.web.context.globalState.action.Action
import io.limberapp.web.util.ComponentWithApi
import io.limberapp.web.util.async
import react.*

internal sealed class OrgRoleMembershipAction : Action() {
  internal data class BeginLoading(val orgRoleGuid: UUID) : OrgRoleMembershipAction()

  internal data class SetValue(
    val orgRoleGuid: UUID,
    val orgRoleMemberships: Set<OrgRoleMembershipRep.Complete>
  ) : OrgRoleMembershipAction()

  internal data class UpdateValue(
    val orgRoleGuid: UUID,
    val orgRoleMembership: OrgRoleMembershipRep.Complete
  ) : OrgRoleMembershipAction()

  internal data class DeleteValue(
    val orgRoleGuid: UUID,
    val accountGuid: UUID
  ) : OrgRoleMembershipAction()

  internal data class SetError(
    val orgRoleGuid: UUID,
    val errorMessage: String?
  ) : OrgRoleMembershipAction()
}

internal fun ComponentWithApi.loadOrgRoleMemberships(orgRoleGuid: UUID) {
  val orgGuid = gs.org.loadedState.guid

  useEffect(listOf(orgRoleGuid)) {
    if (gs.orgRoleMemberships[orgRoleGuid]?.hasBegunLoading == true) return@useEffect
    dispatch(OrgRoleMembershipAction.BeginLoading(orgRoleGuid))
    async {
      api.orgRoleMemberships(OrgRoleMembershipApi.GetByOrgRoleGuid(orgGuid, orgRoleGuid)).fold(
        onSuccess = { orgRoleMemberships ->
          dispatch(OrgRoleMembershipAction.SetValue(orgRoleGuid, orgRoleMemberships))
        },
        onFailure = {
          dispatch(OrgRoleMembershipAction.SetError(orgRoleGuid, it.message))
        }
      )
    }
  }
}

internal suspend fun ComponentWithApi.createOrgRoleMembership(orgRoleGuid: UUID, rep: OrgRoleMembershipRep.Creation) {
  api.orgRoleMemberships(
    endpoint = OrgRoleMembershipApi.Post(
      orgGuid = gs.org.loadedState.guid,
      orgRoleGuid = orgRoleGuid,
      rep = rep
    )
  ).fold(
    onSuccess = { orgRoleMembership -> dispatch(OrgRoleMembershipAction.UpdateValue(orgRoleGuid, orgRoleMembership)) },
    onFailure = { dispatch(OrgRoleMembershipAction.SetError(orgRoleGuid, it.message)) }
  )
}

internal suspend fun ComponentWithApi.deleteOrgRoleMembership(orgRoleGuid: UUID, accountGuid: UUID) {
  api.orgRoleMemberships(
    endpoint = OrgRoleMembershipApi.Delete(
      orgGuid = gs.org.loadedState.guid,
      orgRoleGuid = orgRoleGuid,
      accountGuid = accountGuid
    )
  ).fold(
    onSuccess = { dispatch(OrgRoleMembershipAction.DeleteValue(orgRoleGuid, accountGuid)) },
    onFailure = { dispatch(OrgRoleMembershipAction.SetError(orgRoleGuid, it.message)) }
  )
}
