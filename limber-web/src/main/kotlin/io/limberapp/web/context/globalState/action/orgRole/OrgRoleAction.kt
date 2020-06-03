package io.limberapp.web.context.globalState.action.orgRole

import com.piperframework.types.UUID
import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.context.globalState.action.Action
import io.limberapp.web.util.Context
import io.limberapp.web.util.async
import react.*

internal sealed class OrgRoleAction : Action() {
  internal object BeginLoading : OrgRoleAction()

  internal data class SetValue(val orgRoles: Set<OrgRoleRep.Complete>) : OrgRoleAction()

  internal data class UpdateValue(val orgRole: OrgRoleRep.Complete) : OrgRoleAction()

  internal data class DeleteValue(val orgRoleGuid: UUID) : OrgRoleAction()

  internal data class SetError(val errorMessage: String?) : OrgRoleAction()
}

internal fun Context.ensureOrgRolesLoaded(orgGuid: UUID) {
  useEffect(listOf(orgGuid)) {
    if (global.state.orgRoles.hasBegunLoading) return@useEffect
    global.dispatch(OrgRoleAction.BeginLoading)
    async {
      api.orgRoles(OrgRoleApi.GetByOrgGuid(orgGuid)).fold(
        onSuccess = { orgRoles -> global.dispatch(OrgRoleAction.SetValue(orgRoles)) },
        onFailure = { global.dispatch(OrgRoleAction.SetError(it.message)) }
      )
    }
  }
}

internal suspend fun Context.updateOrgRole(orgRoleGuid: UUID, rep: OrgRoleRep.Update) {
  api.orgRoles(
    endpoint = OrgRoleApi.Patch(
      orgGuid = global.state.org.loadedState.guid,
      orgRoleGuid = orgRoleGuid,
      rep = rep
    )
  ).fold(
    onSuccess = { orgRole -> global.dispatch(OrgRoleAction.UpdateValue(orgRole)) },
    onFailure = { global.dispatch(OrgRoleAction.SetError(it.message)) }
  )
}

internal suspend fun Context.deleteOrgRole(orgRoleGuid: UUID) {
  api.orgRoles(OrgRoleApi.Delete(orgGuid = global.state.org.loadedState.guid, orgRoleGuid = orgRoleGuid)).fold(
    onSuccess = { global.dispatch(OrgRoleAction.DeleteValue(orgRoleGuid)) },
    onFailure = { global.dispatch(OrgRoleAction.SetError(it.message)) }
  )
}
