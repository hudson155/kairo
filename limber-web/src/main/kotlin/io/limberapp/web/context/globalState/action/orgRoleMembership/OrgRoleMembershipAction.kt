package io.limberapp.web.context.globalState.action.orgRoleMembership

import com.piperframework.types.UUID
import io.limberapp.backend.module.auth.api.org.role.OrgRoleMembershipApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep
import io.limberapp.web.context.globalState.action.Action
import io.limberapp.web.util.Context
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

internal suspend fun Context.createOrgRoleMembership(orgRoleGuid: UUID, rep: OrgRoleMembershipRep.Creation) {
  api.orgRoleMemberships(
    endpoint = OrgRoleMembershipApi.Post(
      orgGuid = global.state.org.loadedState.guid,
      orgRoleGuid = orgRoleGuid,
      rep = rep
    )
  ).fold(
    onSuccess = { orgRoleMembership ->
      global.dispatch(OrgRoleMembershipAction.UpdateValue(orgRoleGuid, orgRoleMembership))
    },
    onFailure = { global.dispatch(OrgRoleMembershipAction.SetError(orgRoleGuid, it.message)) }
  )
}

internal suspend fun Context.deleteOrgRoleMembership(orgRoleGuid: UUID, accountGuid: UUID) {
  api.orgRoleMemberships(
    endpoint = OrgRoleMembershipApi.Delete(
      orgGuid = global.state.org.loadedState.guid,
      orgRoleGuid = orgRoleGuid,
      accountGuid = accountGuid
    )
  ).fold(
    onSuccess = { global.dispatch(OrgRoleMembershipAction.DeleteValue(orgRoleGuid, accountGuid)) },
    onFailure = { global.dispatch(OrgRoleMembershipAction.SetError(orgRoleGuid, it.message)) }
  )
}

internal fun Context.ensureOrgRoleMembershipsLoaded(orgGuid: UUID, orgRoleGuid: UUID) {
  useEffect(listOf(orgRoleGuid)) {
    if (global.state.orgRoleMemberships[orgRoleGuid]?.hasBegunLoading == true) return@useEffect
    global.dispatch(OrgRoleMembershipAction.BeginLoading(orgRoleGuid))
    async {
      api.orgRoleMemberships(OrgRoleMembershipApi.GetByOrgRoleGuid(orgGuid, orgRoleGuid)).fold(
        onSuccess = { orgRoleMemberships ->
          global.dispatch(OrgRoleMembershipAction.SetValue(orgRoleGuid, orgRoleMemberships))
        },
        onFailure = { global.dispatch(OrgRoleMembershipAction.SetError(orgRoleGuid, it.message)) }
      )
    }
  }
}
