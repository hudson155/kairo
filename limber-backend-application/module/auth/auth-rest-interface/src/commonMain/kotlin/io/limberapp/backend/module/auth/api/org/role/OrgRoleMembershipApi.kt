package io.limberapp.backend.module.auth.api.org.role

import com.piperframework.restInterface.HttpMethod
import com.piperframework.restInterface.PiperEndpoint
import com.piperframework.types.UUID
import com.piperframework.util.enc
import io.limberapp.backend.module.auth.rep.org.OrgRoleMembershipRep

@Suppress("StringLiteralDuplication")
object OrgRoleMembershipApi {
    data class Post(val orgGuid: UUID, val orgRoleGuid: UUID, val rep: OrgRoleMembershipRep.Creation?) : PiperEndpoint(
        httpMethod = HttpMethod.POST,
        path = "/orgs/${enc(orgGuid)}/roles/${enc(orgRoleGuid)}/memberships",
        body = rep
    )

    data class GetByOrgRoleGuid(val orgGuid: UUID, val orgRoleGuid: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.GET,
        path = "/orgs/${enc(orgGuid)}/roles/${enc(orgRoleGuid)}/memberships"
    )

    data class Delete(val orgGuid: UUID, val orgRoleGuid: UUID, val accountGuid: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.DELETE,
        path = "/orgs/${enc(orgGuid)}/roles/${enc(orgRoleGuid)}/memberships/${enc(accountGuid)}"
    )
}
