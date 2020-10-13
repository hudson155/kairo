package io.limberapp.backend.module.forms.api.formTemplate

import io.ktor.http.HttpMethod
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.util.url.enc
import java.util.*

@Suppress("StringLiteralDuplication")
object FormTemplateApi {
  data class Post(val featureGuid: UUID, val rep: FormTemplateRep.Creation?) : LimberEndpoint(
      httpMethod = HttpMethod.Post,
      path = "/forms/${enc(featureGuid)}/templates",
      body = rep
  )

  data class Patch(
      val featureGuid: UUID,
      val formTemplateGuid: UUID,
      val rep: FormTemplateRep.Update?,
  ) : LimberEndpoint(
      httpMethod = HttpMethod.Patch,
      path = "/forms/${enc(featureGuid)}/templates/${enc(formTemplateGuid)}",
      body = rep,
  )

  data class Get(val featureGuid: UUID, val formTemplateGuid: UUID) : LimberEndpoint(
      httpMethod = HttpMethod.Get,
      path = "/forms/${enc(featureGuid)}/templates/${enc(formTemplateGuid)}"
  )

  data class GetByFeatureGuid(val featureGuid: UUID) : LimberEndpoint(
      httpMethod = HttpMethod.Get,
      path = "/forms/${enc(featureGuid)}/templates"
  )

  data class Delete(val featureGuid: UUID, val formTemplateGuid: UUID) : LimberEndpoint(
      httpMethod = HttpMethod.Delete,
      path = "/forms/${enc(featureGuid)}/templates/${enc(formTemplateGuid)}"
  )
}
