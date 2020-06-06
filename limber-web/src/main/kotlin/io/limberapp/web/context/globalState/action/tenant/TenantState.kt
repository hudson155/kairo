package io.limberapp.web.context.globalState.action.tenant

import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.web.context.LoadableState

internal typealias TenantState = LoadableState<TenantRep.Complete>

/**
 * [TenantState] is loaded eagerly, so it's ok to access [state] all the time without doing a null check.
 */
internal val LoadableState<TenantRep.Complete>.state get() = checkNotNull(stateOrNull)
