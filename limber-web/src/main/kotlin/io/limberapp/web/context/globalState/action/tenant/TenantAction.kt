package io.limberapp.web.context.globalState.action.tenant

import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.web.context.globalState.action.Action

internal sealed class TenantAction : Action()

internal object BeginLoadingTenant : TenantAction()

internal data class SetTenant(val tenant: TenantRep.Complete) : TenantAction()
