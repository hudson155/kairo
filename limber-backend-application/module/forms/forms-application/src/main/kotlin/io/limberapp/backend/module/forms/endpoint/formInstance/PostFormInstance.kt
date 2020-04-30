package io.limberapp.backend.module.forms.endpoint.formInstance

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.authorization.HasAccessToFormTemplate
import io.limberapp.backend.module.forms.mapper.formInstance.FormInstanceMapper
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceQuestionService
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService

/**
 * Creates a new form instance.
 */
internal class PostFormInstance @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val formTemplateService: FormTemplateService,
    private val formInstanceService: FormInstanceService,
    private val formInstanceQuestionService: FormInstanceQuestionService,
    private val formInstanceMapper: FormInstanceMapper
) : LimberApiEndpoint<FormInstanceApi.Post, FormInstanceRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = FormInstanceApi.Post::class.template()
) {
    override suspend fun determineCommand(call: ApplicationCall) = FormInstanceApi.Post(
        rep = call.getAndValidateBody()
    )

    override suspend fun Handler.handle(command: FormInstanceApi.Post): FormInstanceRep.Complete {
        val rep = command.rep.required()
        HasAccessToFormTemplate(formTemplateService, rep.formTemplateGuid).authorize()
        val formInstance = formInstanceMapper.model(rep)
        formInstanceService.create(formInstance)
        val questions = formInstanceQuestionService.getByFormInstanceGuid(formInstance.guid)
        return formInstanceMapper.completeRep(formInstance, questions)
    }
}
