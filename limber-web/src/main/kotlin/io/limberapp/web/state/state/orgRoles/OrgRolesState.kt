package io.limberapp.web.state.state.orgRoles

import io.limberapp.common.types.UUID
import io.limberapp.backend.module.auth.rep.org.OrgRoleRep

private typealias OrgRoleGuid = UUID
internal typealias OrgRolesState = Map<OrgRoleGuid, OrgRoleRep.Complete>
