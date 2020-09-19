package io.limberapp.backend.module.forms.api.formTemplate.question

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.common.restInterface.HttpMethod
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.util.url.enc
import java.util.*

@Suppress("StringLiteralDuplication")
object FormTemplateQuestionApi {
  data class Post(
    val featureGuid: UUID,
    val formTemplateGuid: UUID,
    val rank: Int? = null,
    val rep: FormTemplateQuestionRep.Creation?,
  ) : LimberEndpoint(
    httpMethod = HttpMethod.POST,
    path = "/forms/${enc(featureGuid)}/templates/${enc(formTemplateGuid)}/questions",
    queryParams = rank?.let { listOf("rank" to it.toString()) } ?: emptyList(),
    body = rep
  )

  data class Patch(
    val featureGuid: UUID,
    val formTemplateGuid: UUID,
    val questionGuid: UUID,
    val rep: FormTemplateQuestionRep.Update?,
  ) : LimberEndpoint(
    httpMethod = HttpMethod.PATCH,
    path = "/forms/${enc(featureGuid)}/templates/${enc(formTemplateGuid)}/questions/${enc(questionGuid)}",
    body = rep
  )

  data class Delete(val featureGuid: UUID, val formTemplateGuid: UUID, val questionGuid: UUID) : LimberEndpoint(
    httpMethod = HttpMethod.DELETE,
    path = "/forms/${enc(featureGuid)}/templates/${enc(formTemplateGuid)}/questions/${enc(questionGuid)}"
  )
}
