package io.limberapp.backend.module.forms.endpoint.formInstance

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
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
) : LimberApiEndpoint<FormInstanceApi.Delete, Unit>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = FormInstanceApi.Delete::class.template()
) {
    override suspend fun determineCommand(call: ApplicationCall) = FormInstanceApi.Delete(
        formInstanceGuid = call.parameters.getAsType(UUID::class, "formInstanceGuid")
    )

    override suspend fun Handler.handle(command: FormInstanceApi.Delete) {
        HasAccessToFormInstance(formInstanceService, command.formInstanceGuid).authorize()
        formInstanceService.delete(command.formInstanceGuid)
    }
}
