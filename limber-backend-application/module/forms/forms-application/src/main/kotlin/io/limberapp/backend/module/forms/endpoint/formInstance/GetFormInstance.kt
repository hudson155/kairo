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
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceQuestionService
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import java.util.UUID

/**
 * Returns a single form instance.
 */
internal class GetFormInstance @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val formInstanceService: FormInstanceService,
    private val formInstanceQuestionService: FormInstanceQuestionService,
    private val formInstanceMapper: FormInstanceMapper
) : LimberApiEndpoint<FormInstanceApi.Get, FormInstanceRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = FormInstanceApi.Get::class.template()
) {
    override suspend fun determineCommand(call: ApplicationCall) = FormInstanceApi.Get(
        formInstanceGuid = call.parameters.getAsType(UUID::class, "formInstanceGuid")
    )

    override suspend fun Handler.handle(command: FormInstanceApi.Get): FormInstanceRep.Complete {
        HasAccessToFormInstance(formInstanceService, command.formInstanceGuid).authorize()
        val formInstance = formInstanceService.get(command.formInstanceGuid) ?: throw FormInstanceNotFound()
        val questions = formInstanceQuestionService.getByFormInstanceGuid(command.formInstanceGuid)
        return formInstanceMapper.completeRep(formInstance, questions)
    }
}
