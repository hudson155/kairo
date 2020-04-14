package io.limberapp.web.context.globalState

import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.web.context.LoadableState

internal data class GlobalStateContext(
    val tenant: LoadableState<TenantRep.Complete>,
    val org: LoadableState<OrgRep.Complete>
)
