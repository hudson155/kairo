package io.limberapp.backend.module.forms.api.formTemplate

import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.common.restInterface.HttpMethod
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.common.util.url.enc

@Suppress("StringLiteralDuplication")
object FormTemplateApi {
  data class Post(val featureGuid: String, val rep: FormTemplateRep.Creation?) : LimberEndpoint(
    httpMethod = HttpMethod.POST,
    path = "/forms/${enc(featureGuid)}/templates",
    body = rep
  )

  data class Patch(
    val featureGuid: String,
    val formTemplateGuid: String,
    val rep: FormTemplateRep.Update?,
  ) : LimberEndpoint(
    httpMethod = HttpMethod.PATCH,
    path = "/forms/${enc(featureGuid)}/templates/${enc(formTemplateGuid)}",
    body = rep,
  )

  data class Get(val featureGuid: String, val formTemplateGuid: String) : LimberEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/forms/${enc(featureGuid)}/templates/${enc(formTemplateGuid)}"
  )

  data class GetByFeatureGuid(val featureGuid: String) : LimberEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/forms/${enc(featureGuid)}/templates"
  )

  data class Delete(val featureGuid: String, val formTemplateGuid: String) : LimberEndpoint(
    httpMethod = HttpMethod.DELETE,
    path = "/forms/${enc(featureGuid)}/templates/${enc(formTemplateGuid)}"
  )
}
