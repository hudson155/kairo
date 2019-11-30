package io.limberapp.backend.module.orgs.endpoint.org.feature

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.command.AbstractCommand
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.orgs.service.org.OrgService
import java.util.UUID

/**
 * Deletes a feature from an org. This does not delete the feature's implementation in the corresponding module. The
 * implementation is not deleted, in case it needs to be recovered.
 */
internal class DeleteFeature @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val orgService: OrgService
) : LimberApiEndpoint<DeleteFeature.Command, Unit>(
    application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val orgId: UUID,
        val featureId: UUID
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        orgId = call.parameters.getAsType(UUID::class, orgId),
        featureId = call.parameters.getAsType(UUID::class, featureId)
    )

    override fun authorization(command: Command) = Authorization.OrgMember(command.orgId)

    override suspend fun handler(command: Command) {
        orgService.deleteFeature(
            orgId = command.orgId,
            featureId = command.featureId
        )
    }

    companion object {
        const val orgId = "orgId"
        const val featureId = "featureId"
        val endpointConfig = EndpointConfig(HttpMethod.Delete, "/orgs/{$orgId}/features/{$featureId}")
    }
}
