package io.limberapp.web.context.globalState.action.orgRoleMemberships

import com.piperframework.types.UUID
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep
import io.limberapp.web.context.LoadableState

internal typealias OrgRoleMembershipsState = Map<UUID, LoadableState<Map<UUID, OrgRoleMembershipRep.Complete>>>
