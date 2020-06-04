package io.limberapp.web.context.globalState.action.tenant

import io.limberapp.backend.module.auth.api.tenant.TenantApi
import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.api.Api
import io.limberapp.web.context.globalState.action.Action
import io.limberapp.web.util.ComponentWithGlobalState
import io.limberapp.web.util.async
import react.*

internal sealed class TenantAction : Action() {
  internal object BeginLoading : TenantAction()

  internal data class SetValue(val tenant: TenantRep.Complete) : TenantAction()

  internal data class SetError(val errorMessage: String?) : TenantAction()
}

private typealias State = LoadableState<TenantRep.Complete>

internal fun ComponentWithGlobalState.load(@Suppress("UNUSED_PARAMETER") state: State, api: Api, domain: String) {
  useEffect(listOf(domain)) {
    if (gs.tenant.hasBegunLoading) return@useEffect
    dispatch(TenantAction.BeginLoading)
    async {
      api.tenants(TenantApi.GetByDomain(domain)).fold(
        onSuccess = { tenant -> dispatch(TenantAction.SetValue(tenant)) },
        onFailure = { dispatch(TenantAction.SetError(it.message)) }
      )
    }
  }
}
