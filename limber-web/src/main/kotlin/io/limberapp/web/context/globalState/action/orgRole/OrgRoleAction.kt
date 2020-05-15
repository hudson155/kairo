package io.limberapp.web.context.globalState.action.orgRole

import com.piperframework.types.UUID
import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.context.globalState.action.Action
import io.limberapp.web.util.EnsureLoadedContext
import io.limberapp.web.util.async
import react.*

internal sealed class OrgRoleAction : Action() {
  internal object BeginLoading : OrgRoleAction()

  internal data class SetValue(val orgRoles: Set<OrgRoleRep.Complete>) : OrgRoleAction()

  internal data class UpdateValue(val orgRole: OrgRoleRep.Complete) : OrgRoleAction()
}

internal fun EnsureLoadedContext.ensureOrgRolesLoaded(orgGuid: UUID) {
  useEffect(listOf(orgGuid)) {
    if (global.state.orgRoles.hasBegunLoading) return@useEffect
    global.dispatch(OrgRoleAction.BeginLoading)
    async {
      val orgRoles = api.orgRoles(OrgRoleApi.GetByOrgGuid(orgGuid))
      global.dispatch(OrgRoleAction.SetValue(orgRoles))
    }
  }
}
