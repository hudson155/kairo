package io.limberapp.backend.module.forms.endpoint.formInstance

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.StringComponent
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.VariableComponent
import com.piperframework.endpoint.command.AbstractCommand
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.authorization.MemberOfOrgThatOwnsFormInstance
import io.limberapp.backend.module.forms.exception.formInstance.FormInstanceNotFound
import io.limberapp.backend.module.forms.mapper.formInstance.FormInstanceMapper
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import java.util.UUID

/**
 * Returns a single form instance.
 */
internal class GetFormInstance @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val formInstanceService: FormInstanceService,
    private val formInstanceMapper: FormInstanceMapper
) : LimberApiEndpoint<GetFormInstance.Command, FormInstanceRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val formInstanceId: UUID
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        formInstanceId = call.parameters.getAsType(UUID::class, formInstanceId)
    )

    override suspend fun Handler.handle(command: Command): FormInstanceRep.Complete {
        MemberOfOrgThatOwnsFormInstance(formInstanceService, command.formInstanceId).authorize()
        val model = formInstanceService.get(command.formInstanceId) ?: throw FormInstanceNotFound()
        return formInstanceMapper.completeRep(model)
    }

    companion object {
        const val formInstanceId = "formInstanceId"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Get,
            pathTemplate = listOf(StringComponent("form-instances"), VariableComponent(formInstanceId))
        )
    }
}
