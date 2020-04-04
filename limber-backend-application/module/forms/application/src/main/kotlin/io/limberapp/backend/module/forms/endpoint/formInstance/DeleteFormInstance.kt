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
import io.limberapp.backend.module.forms.authorization.HasAccessToFormInstance
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import java.util.UUID

/**
 * Deletes an existing form instance.
 */
internal class DeleteFormInstance @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val formInstanceService: FormInstanceService
) : LimberApiEndpoint<DeleteFormInstance.Command, Unit>(
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

    override suspend fun Handler.handle(command: Command) {
        HasAccessToFormInstance(formInstanceService, command.formInstanceId).authorize()
        formInstanceService.delete(command.formInstanceId)
    }

    companion object {
        const val formInstanceId = "formInstanceId"
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Delete,
            pathTemplate = listOf(StringComponent("form-instances"), VariableComponent(formInstanceId))
        )
    }
}
