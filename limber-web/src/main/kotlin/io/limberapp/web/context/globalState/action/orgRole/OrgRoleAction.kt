package io.limberapp.web.context.globalState.action.orgRole

import com.piperframework.types.UUID
import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.context.globalState.action.Action
import io.limberapp.web.util.ComponentWithApi
import io.limberapp.web.util.async
import react.*

internal sealed class OrgRoleAction : Action() {
  internal object BeginLoading : OrgRoleAction()

  internal data class SetValue(val orgRoles: Set<OrgRoleRep.Complete>) : OrgRoleAction()

  internal data class UpdateValue(val orgRole: OrgRoleRep.Complete) : OrgRoleAction()

  internal data class DeleteValue(val orgRoleGuid: UUID) : OrgRoleAction()

  internal data class SetError(val errorMessage: String?) : OrgRoleAction()
}

internal fun ComponentWithApi.load(@Suppress("UNUSED_PARAMETER") state: OrgRoleState) {
  val orgGuid = gs.org.loadedState.guid

  useEffect(listOf(orgGuid)) {
    if (gs.orgRoles.hasBegunLoading) return@useEffect
    dispatch(OrgRoleAction.BeginLoading)
    async {
      api.orgRoles(OrgRoleApi.GetByOrgGuid(orgGuid)).fold(
        onSuccess = { orgRoles -> dispatch(OrgRoleAction.SetValue(orgRoles)) },
        onFailure = { dispatch(OrgRoleAction.SetError(it.message)) }
      )
    }
  }
}

internal suspend fun ComponentWithApi.updateOrgRole(orgRoleGuid: UUID, rep: OrgRoleRep.Update) {
  api.orgRoles(
    endpoint = OrgRoleApi.Patch(
      orgGuid = gs.org.loadedState.guid,
      orgRoleGuid = orgRoleGuid,
      rep = rep
    )
  ).fold(
    onSuccess = { orgRole -> dispatch(OrgRoleAction.UpdateValue(orgRole)) },
    onFailure = { dispatch(OrgRoleAction.SetError(it.message)) }
  )
}

internal suspend fun ComponentWithApi.deleteOrgRole(orgRoleGuid: UUID) {
  api.orgRoles(OrgRoleApi.Delete(orgGuid = gs.org.loadedState.guid, orgRoleGuid = orgRoleGuid)).fold(
    onSuccess = { dispatch(OrgRoleAction.DeleteValue(orgRoleGuid)) },
    onFailure = { dispatch(OrgRoleAction.SetError(it.message)) }
  )
}
