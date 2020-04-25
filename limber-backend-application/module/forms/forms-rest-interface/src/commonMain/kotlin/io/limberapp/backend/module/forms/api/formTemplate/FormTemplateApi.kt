package io.limberapp.backend.module.forms.api.formTemplate

import com.piperframework.restInterface.HttpMethod
import com.piperframework.restInterface.PiperEndpoint
import com.piperframework.types.UUID
import com.piperframework.util.enc
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep

@Suppress("StringLiteralDuplication")
object FormTemplateApi {
    data class Post(val rep: FormTemplateRep.Creation?) : PiperEndpoint(
        httpMethod = HttpMethod.POST,
        path = "/form-templates",
        body = rep
    )

    data class Patch(val formTemplateGuid: UUID, val rep: FormTemplateRep.Update?) : PiperEndpoint(
        httpMethod = HttpMethod.PATCH,
        path = "/form-templates/${enc(formTemplateGuid)}",
        body = rep
    )

    data class Get(val formTemplateGuid: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.GET,
        path = "/form-templates/${enc(formTemplateGuid)}"
    )

    data class GetByFeatureGuid(val featureGuid: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.GET,
        path = "/form-templates",
        queryParams = listOf("featureGuid" to enc(featureGuid))
    )

    data class Delete(val formTemplateGuid: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.DELETE,
        path = "/form-templates/${enc(formTemplateGuid)}"
    )
}
