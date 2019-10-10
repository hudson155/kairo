package io.limberapp.backend.module.orgs.endpoint.formTemplate

import com.google.inject.Inject
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.ktor.request.receive
import io.limberapp.backend.module.orgs.model.formTemplate.Org
import io.limberapp.backend.module.orgs.service.formTemplate.OrgService
import io.limberapp.framework.endpoint.ModelApiEndpoint

/**
 * Creates a new org.
 */
internal class CreateOrg @Inject constructor(
    application: Application,
    private val orgService: OrgService
) : ModelApiEndpoint<Org>(application, config) {

    override suspend fun handler(call: ApplicationCall): Org {
        return orgService.create(
            model = call.receive()
        )
    }

    companion object {
        private val config = Config(HttpMethod.Post, "/orgs")
    }
}
