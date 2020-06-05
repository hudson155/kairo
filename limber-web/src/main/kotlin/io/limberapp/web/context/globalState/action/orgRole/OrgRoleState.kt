package io.limberapp.web.context.globalState.action.orgRole

import com.piperframework.types.UUID
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.context.LoadableState

internal typealias OrgRoleState = LoadableState<Map<UUID, OrgRoleRep.Complete>>
