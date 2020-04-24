package io.limberapp.backend.module.forms.api.formInstance.question

import com.piperframework.restInterface.HttpMethod
import com.piperframework.restInterface.PiperEndpoint
import com.piperframework.types.UUID
import com.piperframework.util.enc
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep

@Suppress("StringLiteralDuplication")
object FormInstanceQuestionApi {
    data class Put(
        val formInstanceId: UUID,
        val questionId: UUID,
        val rep: FormInstanceQuestionRep.Creation?
    ) : PiperEndpoint(
        httpMethod = HttpMethod.PUT,
        path = "/form-instances/${enc(formInstanceId)}/questions/${enc(questionId)}",
        body = rep
    )

    data class Delete(val formInstanceId: UUID, val questionId: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.DELETE,
        path = "/form-instances/${enc(formInstanceId)}/questions/${enc(questionId)}"
    )
}
