package io.limberapp.web.context.globalState.action.orgRoles

import com.piperframework.types.UUID
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep
import io.limberapp.web.context.LoadableState

internal typealias OrgRolesState = LoadableState<Map<UUID, OrgRoleRep.Complete>>
