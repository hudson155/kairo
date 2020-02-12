package io.limberapp.backend.module.forms.endpoint.formInstance

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.StringComponent
import com.piperframework.endpoint.command.AbstractCommand
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.mapper.formInstance.FormInstanceMapper
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import java.util.UUID

/**
 * Returns all form instances within the org.
 */
internal class GetFormInstancesByOrgId @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val formInstanceService: FormInstanceService,
    private val formInstanceMapper: FormInstanceMapper
) : LimberApiEndpoint<GetFormInstancesByOrgId.Command, List<FormInstanceRep.Complete>>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val orgId: UUID
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        orgId = call.parameters.getAsType(UUID::class, orgId)
    )

    override suspend fun Handler.handle(command: Command): List<FormInstanceRep.Complete> {
        Authorization.OrgMember(command.orgId).authorize()
        val models = formInstanceService.getByOrgId(command.orgId)
        return models.map { formInstanceMapper.completeRep(it) }
    }

    companion object {
        const val orgId = "orgId"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Get,
            pathTemplate = listOf(StringComponent("form-instances"))
        )
    }
}
