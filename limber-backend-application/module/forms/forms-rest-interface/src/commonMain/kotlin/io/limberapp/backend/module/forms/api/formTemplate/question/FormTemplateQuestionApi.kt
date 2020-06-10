package io.limberapp.backend.module.forms.api.formTemplate.question

import com.piperframework.restInterface.HttpMethod
import com.piperframework.restInterface.PiperEndpoint
import com.piperframework.types.UUID
import com.piperframework.util.enc
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep

@Suppress("StringLiteralDuplication")
object FormTemplateQuestionApi {
  data class Post(
    val featureGuid: UUID,
    val formTemplateGuid: UUID,
    val rank: Int? = null,
    val rep: FormTemplateQuestionRep.Creation?
  ) : PiperEndpoint(
    httpMethod = HttpMethod.POST,
    path = "/forms/${enc(featureGuid)}/templates/${enc(formTemplateGuid)}/questions",
    queryParams = rank?.let { listOf("rank" to it.toString()) } ?: emptyList(),
    body = rep
  )

  data class Patch(
    val featureGuid: UUID,
    val formTemplateGuid: UUID,
    val questionGuid: UUID,
    val rep: FormTemplateQuestionRep.Update?
  ) : PiperEndpoint(
    httpMethod = HttpMethod.PATCH,
    path = "/forms/${enc(featureGuid)}/templates/${enc(formTemplateGuid)}/questions/${enc(questionGuid)}",
    body = rep
  )

  data class Delete(val featureGuid: UUID, val formTemplateGuid: UUID, val questionGuid: UUID) : PiperEndpoint(
    httpMethod = HttpMethod.DELETE,
    path = "/forms/${enc(featureGuid)}/templates/${enc(formTemplateGuid)}/questions/${enc(questionGuid)}"
  )
}
