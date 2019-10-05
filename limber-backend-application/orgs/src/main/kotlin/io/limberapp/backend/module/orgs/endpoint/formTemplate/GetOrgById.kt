package io.limberapp.backend.module.orgs.endpoint.formTemplate

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.module.orgs.model.formTemplate.Org
import io.limberapp.backend.module.orgs.service.formTemplate.OrgService
import io.limberapp.framework.endpoint.NullableModelApiEndpoint
import java.util.UUID

/**
 * Gets an org by its unique ID.
 */
internal class GetOrgById @Inject constructor(
    application: Application,
    private val orgService: OrgService
) : NullableModelApiEndpoint<Org>(application, config) {

    override suspend fun handler(call: ApplicationCall): Org? {
        return orgService.getById(
            id = call.parameters.getAsType(UUID::class, "orgId")
        )
    }

    companion object {
        private val config = Config(HttpMethod.Get, "/orgs/{orgId}")
    }
}
