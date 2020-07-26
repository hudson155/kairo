package io.limberapp.backend.module.forms.api.formInstance

import com.piperframework.restInterface.HttpMethod
import com.piperframework.restInterface.PiperEndpoint
import com.piperframework.types.UUID
import com.piperframework.util.enc
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep

@Suppress("StringLiteralDuplication")
object FormInstanceApi {
  data class Post(val featureGuid: UUID, val rep: FormInstanceRep.Creation?) : PiperEndpoint(
    httpMethod = HttpMethod.POST,
    path = "/forms/${enc(featureGuid)}/instances",
    body = rep
  )

  data class Get(val featureGuid: UUID, val formInstanceGuid: UUID) : PiperEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/forms/${enc(featureGuid)}/instances/${enc(formInstanceGuid)}"
  )

  data class GetByFeatureGuid(val featureGuid: UUID, val creatorAccountGuid: UUID? = null) : PiperEndpoint(
    httpMethod = HttpMethod.GET,
    path = "/forms/${enc(featureGuid)}/instances",
    queryParams = listOfNotNull(creatorAccountGuid?.let { "creatorAccountGuid" to enc(it) })
  )

  data class Delete(val featureGuid: UUID, val formInstanceGuid: UUID) : PiperEndpoint(
    httpMethod = HttpMethod.DELETE,
    path = "/forms/${enc(featureGuid)}/instances/${enc(formInstanceGuid)}"
  )
}
