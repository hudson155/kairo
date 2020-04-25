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

    data class Get(val formInstanceGuid: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.GET,
        path = "/form-instances/${enc(formInstanceGuid)}"
    )

    data class GetByFeatureGuid(val featureGuid: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.GET,
        path = "/form-instances",
        queryParams = listOf("featureGuid" to enc(featureGuid))
    )

    data class Delete(val formInstanceGuid: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.DELETE,
        path = "/form-instances/${enc(formInstanceGuid)}"
    )
}
