package io.limberapp.backend.module.forms.api.formInstance.question

import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.common.restInterface.HttpMethod
import io.limberapp.common.restInterface.LimberEndpoint
import io.limberapp.common.util.url.enc
import java.util.*

@Suppress("StringLiteralDuplication")
object FormInstanceQuestionApi {
  data class Put(
    val featureGuid: UUID,
    val formInstanceGuid: UUID,
    val questionGuid: UUID,
    val rep: FormInstanceQuestionRep.Creation?,
  ) : LimberEndpoint(
    httpMethod = HttpMethod.PUT,
    path = "/forms/${enc(featureGuid)}/instances/${enc(formInstanceGuid)}/questions/${enc(questionGuid)}",
    body = rep
  )

  data class Delete(val featureGuid: UUID, val formInstanceGuid: UUID, val questionGuid: UUID) : LimberEndpoint(
    httpMethod = HttpMethod.DELETE,
    path = "/forms/${enc(featureGuid)}/instances/${enc(formInstanceGuid)}/questions/${enc(questionGuid)}"
  )
}
