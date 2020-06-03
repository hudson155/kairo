package io.limberapp.web.context.globalState.action.orgRoleMembership

import com.piperframework.types.UUID
import io.limberapp.backend.module.auth.api.org.role.OrgRoleMembershipApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep
import io.limberapp.web.context.globalState.action.Action
import io.limberapp.web.util.EnsureLoadedContext
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

internal fun EnsureLoadedContext.ensureOrgRoleMembershipsLoaded(orgGuid: UUID, orgRoleGuid: UUID) {
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
