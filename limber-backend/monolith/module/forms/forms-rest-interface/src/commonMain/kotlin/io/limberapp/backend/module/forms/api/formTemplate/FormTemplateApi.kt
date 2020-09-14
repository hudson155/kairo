package io.limberapp.backend.module.forms.api.formTemplate

import com.piperframework.restInterface.HttpMethod
import com.piperframework.restInterface.PiperEndpoint
import com.piperframework.types.UUID
import com.piperframework.util.enc
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep

@Suppress("StringLiteralDuplication")
object FormTemplateApi {
  data class Post(val featureGuid: UUID, val rep: FormTemplateRep.Creation?) : PiperEndpoint(
    httpMethod = HttpMethod.POST,
    path = "/forms/${enc(featureGuid)}/templates",
    body = rep
  )

  data class Patch(val featureGuid: UUID, val formTemplateGuid: UUID, val rep: FormTemplateRep.Update?) : PiperEndpoint(
    httpMethod = HttpMethod.PATCH,
    path = "/forms/${enc(featureGuid)}/templates/${enc(formTemplateGuid)}",
    body = rep
  )

  data class Get(val featureGuid: UUID, val formTemplateGuid: UUID) : PiperEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/forms/${enc(featureGuid)}/templates/${enc(formTemplateGuid)}"
  )

  data class GetByFeatureGuid(val featureGuid: UUID) : PiperEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/forms/${enc(featureGuid)}/templates"
  )

  data class Delete(val featureGuid: UUID, val formTemplateGuid: UUID) : PiperEndpoint(
    httpMethod = HttpMethod.DELETE,
    path = "/forms/${enc(featureGuid)}/templates/${enc(formTemplateGuid)}"
  )
}
