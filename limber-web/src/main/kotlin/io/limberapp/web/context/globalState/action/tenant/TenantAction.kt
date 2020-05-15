package io.limberapp.web.context.globalState.action.tenant

import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.web.context.globalState.action.Action
import io.limberapp.web.util.EnsureLoadedContext
import io.limberapp.web.util.async
import react.useEffect

internal sealed class TenantAction : Action() {
  internal object BeginLoading : TenantAction()

  internal data class SetValue(val tenant: TenantRep.Complete) : TenantAction()
}

internal fun EnsureLoadedContext.ensureTenantLoaded(domain: String) {
  useEffect(listOf(domain)) {
    if (global.state.tenant.hasBegunLoading) return@useEffect
    global.dispatch(TenantAction.BeginLoading)
    async {
      val tenant = api.tenants(TenantApi.GetByDomain(domain))
      global.dispatch(TenantAction.SetValue(tenant))
    }
  }
}
