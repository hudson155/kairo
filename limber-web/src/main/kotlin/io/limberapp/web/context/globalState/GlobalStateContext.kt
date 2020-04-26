package io.limberapp.web.context.globalState

import io.limberapp.backend.module.auth.rep.tenant.TenantRep
import io.limberapp.backend.module.orgs.rep.org.OrgRep
import io.limberapp.backend.module.users.rep.account.UserRep
import io.limberapp.web.context.LoadableState

internal data class GlobalStateContext(
    val org: LoadableState<OrgRep.Complete>,
    val tenant: LoadableState<TenantRep.Complete>,
    val user: LoadableState<UserRep.Complete>
)
