package io.limberapp.web.context.globalState.action.orgRoles

import com.piperframework.types.UUID
import io.limberapp.backend.module.auth.api.org.role.OrgRoleApi
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.context.globalState.action.Action
import io.limberapp.web.context.globalState.action.org.state
import io.limberapp.web.util.ComponentWithApi
import io.limberapp.web.util.async
import react.*

internal sealed class OrgRolesAction : Action() {
  internal object BeginLoading : OrgRolesAction()

  internal data class SetValue(val orgRoles: Set<OrgRoleRep.Complete>) : OrgRolesAction()

  internal data class UpdateValue(val orgRole: OrgRoleRep.Complete) : OrgRolesAction()

  internal data class DeleteValue(val orgRoleGuid: UUID) : OrgRolesAction()

  internal data class SetError(val errorMessage: String?) : OrgRolesAction()
}

internal fun ComponentWithApi.loadOrgRoles() {
  val orgGuid = gs.org.state.guid

  useEffect(listOf(orgGuid)) {
    if (gs.orgRoles.hasBegunLoading) return@useEffect
    dispatch(OrgRolesAction.BeginLoading)
    async {
      api.orgRoles(OrgRoleApi.GetByOrgGuid(orgGuid)).fold(
        onSuccess = { orgRoles -> dispatch(OrgRolesAction.SetValue(orgRoles)) },
        onFailure = { dispatch(OrgRolesAction.SetError(it.message)) }
      )
    }
  }
}

internal suspend fun ComponentWithApi.createOrgRole(rep: OrgRoleRep.Creation) {
  api.orgRoles(
    endpoint = OrgRoleApi.Post(
      orgGuid = gs.org.state.guid,
      rep = rep
    )
  ).fold(
    onSuccess = { orgRole -> dispatch(OrgRolesAction.UpdateValue(orgRole)) },
    onFailure = { dispatch(OrgRolesAction.SetError(it.message)) }
  )
}

internal suspend fun ComponentWithApi.updateOrgRole(orgRoleGuid: UUID, rep: OrgRoleRep.Update) {
  api.orgRoles(
    endpoint = OrgRoleApi.Patch(
      orgGuid = gs.org.state.guid,
      orgRoleGuid = orgRoleGuid,
      rep = rep
    )
  ).fold(
    onSuccess = { orgRole -> dispatch(OrgRolesAction.UpdateValue(orgRole)) },
    onFailure = { dispatch(OrgRolesAction.SetError(it.message)) }
  )
}

internal suspend fun ComponentWithApi.deleteOrgRole(orgRoleGuid: UUID) {
  api.orgRoles(OrgRoleApi.Delete(orgGuid = gs.org.state.guid, orgRoleGuid = orgRoleGuid)).fold(
    onSuccess = { dispatch(OrgRolesAction.DeleteValue(orgRoleGuid)) },
    onFailure = { dispatch(OrgRolesAction.SetError(it.message)) }
  )
}
