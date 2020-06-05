package io.limberapp.web.context.globalState.action.org

import com.piperframework.types.UUID
import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.web.context.globalState.action.Action
import io.limberapp.web.util.ComponentWithApi
import io.limberapp.web.util.async
import react.*

internal sealed class OrgAction : Action() {
  internal object BeginLoading : OrgAction()

  internal data class SetValue(val org: OrgRep.Complete) : OrgAction()

  internal data class SetError(val errorMessage: String?) : OrgAction()
}

internal fun ComponentWithApi.load(@Suppress("UNUSED_PARAMETER") state: OrgState, orgGuid: UUID) {
  useEffect(listOf(orgGuid)) {
    if (gs.org.hasBegunLoading) return@useEffect
    dispatch(OrgAction.BeginLoading)
    async {
      api.orgs(OrgApi.Get(orgGuid)).fold(
        onSuccess = { org -> dispatch(OrgAction.SetValue(org)) },
        onFailure = { dispatch(OrgAction.SetError(it.message)) }
      )
    }
  }
}
