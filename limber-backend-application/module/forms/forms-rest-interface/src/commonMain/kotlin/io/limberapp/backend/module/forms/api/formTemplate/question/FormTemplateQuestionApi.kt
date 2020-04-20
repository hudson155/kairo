package io.limberapp.backend.module.forms.api.formTemplate.question

import com.piperframework.restInterface.HttpMethod
import com.piperframework.restInterface.PiperEndpoint
import com.piperframework.types.UUID
import com.piperframework.util.enc
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep

@Suppress("StringLiteralDuplication")
object FormTemplateQuestionApi {

    data class Post(
        val formTemplateId: UUID,
        val rank: Int? = null,
        val rep: FormTemplateQuestionRep.Creation?
    ) : PiperEndpoint(
        httpMethod = HttpMethod.POST,
        path = "/form-templates/${enc(formTemplateId)}/questions",
        queryParams = rank?.let { listOf("rank" to it.toString()) } ?: emptyList(),
        body = rep
    )

    data class Patch(
        val formTemplateId: UUID,
        val questionId: UUID,
        val rep: FormTemplateQuestionRep.Update?
    ) : PiperEndpoint(
        httpMethod = HttpMethod.PATCH,
        path = "/form-templates/${enc(formTemplateId)}/questions/${enc(questionId)}",
        body = rep
    )

    data class Delete(val formTemplateId: UUID, val questionId: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.DELETE,
        path = "/form-templates/${enc(formTemplateId)}/questions/${enc(questionId)}"
    )
}
