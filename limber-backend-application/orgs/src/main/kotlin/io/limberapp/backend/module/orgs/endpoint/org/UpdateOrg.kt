package io.limberapp.backend.module.orgs.endpoint.org

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.ktor.request.receive
import io.limberapp.backend.module.orgs.mapper.OrgMapper
import io.limberapp.backend.module.orgs.rep.formTemplate.OrgRep
import io.limberapp.backend.module.orgs.service.formTemplate.OrgService
import io.limberapp.framework.endpoint.RepApiEndpoint
import java.util.UUID

internal class UpdateOrg @Inject constructor(
    application: Application,
    private val orgService: OrgService
) : RepApiEndpoint<OrgRep.Complete>(application, config) {

    override suspend fun handler(call: ApplicationCall): OrgRep.Complete {
        val orgId = call.parameters.getAsType(UUID::class, "orgId")
        val creationModel = OrgMapper.updateModel(call.receive())
        val completeModel = orgService.update(orgId, creationModel)
        return OrgMapper.completeRep(completeModel)
    }

    companion object {
        private val config = Config(HttpMethod.Patch, "/orgs/{orgId}")
    }
}
