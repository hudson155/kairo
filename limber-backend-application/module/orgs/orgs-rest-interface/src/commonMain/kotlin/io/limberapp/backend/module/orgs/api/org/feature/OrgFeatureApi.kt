package io.limberapp.backend.module.orgs.api.org.feature

import com.piperframework.restInterface.HttpMethod
import com.piperframework.restInterface.PiperEndpoint
import com.piperframework.types.UUID
import com.piperframework.util.enc
import io.limberapp.backend.module.orgs.rep.org.FeatureRep

@Suppress("StringLiteralDuplication")
object OrgFeatureApi {
    data class Post(val orgId: UUID, val rep: FeatureRep.Creation?) : PiperEndpoint(
        httpMethod = HttpMethod.POST,
        path = "/orgs/${enc(orgId)}/features",
        body = rep
    )

    data class Patch(val orgId: UUID, val featureId: UUID, val rep: FeatureRep.Update?) : PiperEndpoint(
        httpMethod = HttpMethod.PATCH,
        path = "/orgs/${enc(orgId)}/features/${enc(featureId)}",
        body = rep
    )

    data class Delete(val orgId: UUID, val featureId: UUID) : PiperEndpoint(
        httpMethod = HttpMethod.DELETE,
        path = "/orgs/${enc(orgId)}/features/${enc(featureId)}"
    )
}
