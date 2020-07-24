package io.limberapp.web.state.state.orgRoleMemberships

import com.piperframework.types.UUID
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep

private typealias AccountGuid = UUID
internal typealias OrgRoleMembershipsState = Map<AccountGuid, OrgRoleMembershipRep.Complete>
