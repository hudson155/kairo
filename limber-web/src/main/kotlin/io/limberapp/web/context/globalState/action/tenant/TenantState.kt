package io.limberapp.web.context.globalState.action.tenant

import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.web.context.LoadableState

internal typealias TenantState = LoadableState<TenantRep.Complete>
