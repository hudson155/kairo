package io.limberapp.backend.module.forms.api.formInstance

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.common.restInterface.ContentType
import io.limberapp.common.restInterface.HttpMethod
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.common.types.TimeZone
import io.limberapp.common.types.UUID
import io.limberapp.common.util.url.enc

@Suppress("StringLiteralDuplication")
object FormInstanceApi {
  data class Post(val featureGuid: UUID, val rep: FormInstanceRep.Creation?) : LimberEndpoint(
    httpMethod = HttpMethod.POST,
    path = "/forms/${enc(featureGuid)}/instances",
    body = rep
  )

  data class Get(val featureGuid: UUID, val formInstanceGuid: UUID) : LimberEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/forms/${enc(featureGuid)}/instances/${enc(formInstanceGuid)}"
  )

  data class GetByFeatureGuid(val featureGuid: UUID, val creatorAccountGuid: UUID? = null) : LimberEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/forms/${enc(featureGuid)}/instances",
    queryParams = listOfNotNull(creatorAccountGuid?.let { "creatorAccountGuid" to enc(it) })
  )

  data class ExportByFeatureGuid(
    val featureGuid: UUID,
    val creatorAccountGuid: UUID? = null,
    val timeZone: TimeZone? = null,
  ) : LimberEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/forms/${enc(featureGuid)}/instances",
    queryParams = listOfNotNull(
      creatorAccountGuid?.let { "creatorAccountGuid" to enc(it) },
      timeZone?.let { "timeZone" to enc(it) }
    ),
    contentType = ContentType.CSV
  )

  data class Patch(
    val featureGuid: UUID,
    val formInstanceGuid: UUID,
    val rep: FormInstanceRep.Update?,
  ) : LimberEndpoint(
    httpMethod = HttpMethod.PATCH,
    path = "/forms/${enc(featureGuid)}/instances/${enc(formInstanceGuid)}",
    body = rep,
  )

  data class Delete(val featureGuid: UUID, val formInstanceGuid: UUID) : LimberEndpoint(
    httpMethod = HttpMethod.DELETE,
    path = "/forms/${enc(featureGuid)}/instances/${enc(formInstanceGuid)}"
  )
}
