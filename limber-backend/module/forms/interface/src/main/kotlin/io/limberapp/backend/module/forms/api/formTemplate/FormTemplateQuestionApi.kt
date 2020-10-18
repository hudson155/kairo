package io.limberapp.backend.module.forms.api.formTemplate

import io.ktor.http.HttpMethod
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.common.util.url.enc
import java.util.*

@Suppress("StringLiteralDuplication")
object FormTemplateQuestionApi {
  data class Post(
      val featureGuid: UUID,
      val formTemplateGuid: UUID,
      val rank: Int? = null,
      val rep: FormTemplateQuestionRep.Creation?,
  ) : LimberEndpoint(
      httpMethod = HttpMethod.Post,
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
      httpMethod = HttpMethod.Patch,
      path = "/forms/${enc(featureGuid)}/templates/${enc(formTemplateGuid)}/questions/${enc(questionGuid)}",
      body = rep
  )

  data class Delete(val featureGuid: UUID, val formTemplateGuid: UUID, val questionGuid: UUID) : LimberEndpoint(
      httpMethod = HttpMethod.Delete,
      path = "/forms/${enc(featureGuid)}/templates/${enc(formTemplateGuid)}/questions/${enc(questionGuid)}"
  )
}
