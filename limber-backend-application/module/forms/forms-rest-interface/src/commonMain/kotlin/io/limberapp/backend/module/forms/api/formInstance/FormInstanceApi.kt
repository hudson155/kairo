package io.limberapp.backend.module.forms.api.formInstance

import com.piperframework.restInterface.HttpMethod
import com.piperframework.restInterface.PiperEndpoint
import com.piperframework.types.UUID
import com.piperframework.util.enc
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep

@Suppress("StringLiteralDuplication")
object FormInstanceApi {
    data class Post(val rep: FormInstanceRep.Creation?) : PiperEndpoint(
        httpMethod = HttpMethod.POST,
        path = "/form-instances",
        body = rep
    )

    data class Get(val formInstanceId: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.GET,
        path = "/form-instances/${enc(formInstanceId)}"
    )

    data class GetByFeatureId(val featureId: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.GET,
        path = "/form-instances",
        queryParams = listOf("featureId" to enc(featureId))
    )

    data class Delete(val formInstanceId: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.DELETE,
        path = "/form-instances/${enc(formInstanceId)}"
    )
}
