package io.limberapp.backend.module.forms.api.formTemplate.question

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.common.restInterface.HttpMethod
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.common.util.url.enc

@Suppress("StringLiteralDuplication")
object FormTemplateQuestionApi {
  data class Post(
    val featureGuid: String,
    val formTemplateGuid: String,
    val rank: Int? = null,
    val rep: FormTemplateQuestionRep.Creation?,
  ) : LimberEndpoint(
    httpMethod = HttpMethod.POST,
    path = "/forms/${enc(featureGuid)}/templates/${enc(formTemplateGuid)}/questions",
    queryParams = rank?.let { listOf("rank" to it.toString()) } ?: emptyList(),
    body = rep
  )

  data class Patch(
    val featureGuid: String,
    val formTemplateGuid: String,
    val questionGuid: String,
    val rep: FormTemplateQuestionRep.Update?,
  ) : LimberEndpoint(
    httpMethod = HttpMethod.PATCH,
    path = "/forms/${enc(featureGuid)}/templates/${enc(formTemplateGuid)}/questions/${enc(questionGuid)}",
    body = rep
  )

  data class Delete(val featureGuid: String, val formTemplateGuid: String, val questionGuid: String) : LimberEndpoint(
    httpMethod = HttpMethod.DELETE,
    path = "/forms/${enc(featureGuid)}/templates/${enc(formTemplateGuid)}/questions/${enc(questionGuid)}"
  )
}
