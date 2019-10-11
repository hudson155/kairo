package io.limberapp.backend.module.orgs.endpoint.org

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.module.orgs.mapper.OrgMapper
import io.limberapp.backend.module.orgs.rep.formTemplate.OrgRep
import io.limberapp.backend.module.orgs.service.formTemplate.OrgService
import io.limberapp.framework.endpoint.NullableRepApiEndpoint
import java.util.UUID

internal class GetOrgById @Inject constructor(
    application: Application,
    private val orgService: OrgService
) : NullableRepApiEndpoint<OrgRep.Complete>(application, config) {

    override suspend fun handler(call: ApplicationCall): OrgRep.Complete? {
        val orgId = call.parameters.getAsType(UUID::class, "orgId")
        val completeModel = orgService.getById(orgId)
        return completeModel?.let { OrgMapper.completeRep(it) }
    }

    companion object {
        private val config = Config(HttpMethod.Get, "/orgs/{orgId}")
    }
}
