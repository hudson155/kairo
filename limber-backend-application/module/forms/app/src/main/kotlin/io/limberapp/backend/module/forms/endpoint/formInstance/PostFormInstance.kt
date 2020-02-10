package io.limberapp.backend.module.forms.endpoint.formInstance

import com.google.inject.Inject
import com.piperframework.config.serving.ServingConfig
import com.piperframework.endpoint.EndpointConfig
import com.piperframework.endpoint.EndpointConfig.PathTemplateComponent.StringComponent
import com.piperframework.endpoint.command.AbstractCommand
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpMethod
import io.limberapp.backend.endpoint.LimberApiEndpoint
import io.limberapp.backend.module.forms.authorization.MemberOfOrgThatOwnsFormTemplate
import io.limberapp.backend.module.forms.mapper.formInstance.FormInstanceMapper
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.backend.module.forms.service.formInstance.FormInstanceService
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService

/**
 * Creates a new form instance.
 */
internal class PostFormInstance @Inject constructor(
    application: Application,
    servingConfig: ServingConfig,
    private val formInstanceService: FormInstanceService,
    private val formInstanceMapper: FormInstanceMapper,
    private val formTemplateService: FormTemplateService
) : LimberApiEndpoint<PostFormInstance.Command, FormInstanceRep.Complete>(
    application = application,
    pathPrefix = servingConfig.apiPathPrefix,
    endpointConfig = endpointConfig
) {

    internal data class Command(
        val creationRep: FormInstanceRep.Creation
    ) : AbstractCommand()

    override suspend fun determineCommand(call: ApplicationCall) = Command(
        creationRep = call.getAndValidateBody()
    )

    override suspend fun Handler.handle(command: Command): FormInstanceRep.Complete {
        MemberOfOrgThatOwnsFormTemplate(formTemplateService, command.creationRep.formTemplateId).authorize()
        val model = formInstanceMapper.model(command.creationRep)
        formInstanceService.create(model)
        return formInstanceMapper.completeRep(model)
    }

    companion object {
        val endpointConfig = EndpointConfig(
            httpMethod = HttpMethod.Post,
            pathTemplate = listOf(StringComponent("form-instances"))
        )
    }
}
