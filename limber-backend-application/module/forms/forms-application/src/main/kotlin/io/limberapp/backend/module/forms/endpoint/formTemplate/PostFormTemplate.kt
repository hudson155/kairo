package io.limberapp.backend.module.forms.endpoint.formTemplate

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.restInterface.template
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.limberapp.backend.authorization.Authorization
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.mapper.formTemplate.FormTemplateMapper
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateQuestionService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService

/**
 * Creates a new form template.
 */
internal class PostFormTemplate @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val formTemplateService: FormTemplateService,
    private val formTemplateQuestionService: FormTemplateQuestionService,
    private val formTemplateMapper: FormTemplateMapper
) : LimberApiEndpoint<FormTemplateApi.Post, FormTemplateRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointTemplate = FormTemplateApi.Post::class.template()
) {
    override suspend fun determineCommand(call: ApplicationCall) = FormTemplateApi.Post(
        rep = call.getAndValidateBody()
    )

    override suspend fun Handler.handle(command: FormTemplateApi.Post): FormTemplateRep.Complete {
        val rep = command.rep.required()
        Authorization.HasAccessToFeature(rep.featureGuid).authorize()
        val formTemplate = formTemplateMapper.model(rep)
        formTemplateService.create(formTemplate)
        val questions = formTemplateQuestionService.getByFormTemplateGuid(formTemplate.guid)
        return formTemplateMapper.completeRep(formTemplate, questions)
    }
}
