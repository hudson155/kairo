package io.limberapp.web.context.globalState.action.tenant

import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.web.context.globalState.action.Action
import io.limberapp.web.util.EnsureLoadedContext
import io.limberapp.web.util.async
import react.*

internal sealed class TenantAction : Action() {
  internal object BeginLoading : TenantAction()

  internal data class SetValue(val tenant: TenantRep.Complete) : TenantAction()

  internal data class SetError(val errorMessage: String?) : TenantAction()
}

internal fun EnsureLoadedContext.ensureTenantLoaded(domain: String) {
  useEffect(listOf(domain)) {
    if (global.state.tenant.hasBegunLoading) return@useEffect
    global.dispatch(TenantAction.BeginLoading)
    async {
      api.tenants(TenantApi.GetByDomain(domain)).fold(
        onSuccess = { tenant -> global.dispatch(TenantAction.SetValue(tenant)) },
        onFailure = { global.dispatch(TenantAction.SetError(it.message)) }
      )
    }
  }
}
