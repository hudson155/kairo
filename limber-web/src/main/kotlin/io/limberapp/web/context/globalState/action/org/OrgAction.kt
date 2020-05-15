package io.limberapp.web.context.globalState.action.org

import com.piperframework.types.UUID
import io.limberapp.backend.module.orgs.api.org.OrgApi
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.web.context.globalState.action.Action
import io.limberapp.web.util.EnsureLoadedContext
import io.limberapp.web.util.async
import react.*

internal sealed class OrgAction : Action() {
  internal object BeginLoading : OrgAction()

  internal data class SetValue(val org: OrgRep.Complete) : OrgAction()
}

internal fun EnsureLoadedContext.ensureOrgLoaded(orgGuid: UUID) {
  useEffect(listOf(orgGuid)) {
    if (global.state.org.hasBegunLoading) return@useEffect
    global.dispatch(OrgAction.BeginLoading)
    async {
      val org = api.orgs(OrgApi.Get(orgGuid))
      global.dispatch(OrgAction.SetValue(org))
    }
  }
}
