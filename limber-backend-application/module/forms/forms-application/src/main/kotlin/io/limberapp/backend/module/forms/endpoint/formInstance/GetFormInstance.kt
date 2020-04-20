package io.limberapp.backend.module.forms.endpoint.formInstance

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.authorization.HasAccessToFormInstance
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
) : LimberApiEndpoint<FormInstanceApi.Get, FormInstanceRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = FormInstanceApi.Get::class.template()
) {

    override suspend fun determineCommand(call: ApplicationCall) = FormInstanceApi.Get(
        formInstanceId = call.parameters.getAsType(UUID::class, "formInstanceId")
    )

    override suspend fun Handler.handle(command: FormInstanceApi.Get): FormInstanceRep.Complete {
        HasAccessToFormInstance(formInstanceService, command.formInstanceId).authorize()
        val model = formInstanceService.get(command.formInstanceId) ?: throw FormInstanceNotFound()
        return formInstanceMapper.completeRep(model)
    }
}
