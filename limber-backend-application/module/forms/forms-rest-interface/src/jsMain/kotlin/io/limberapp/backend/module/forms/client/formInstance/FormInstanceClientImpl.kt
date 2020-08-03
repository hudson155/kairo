package io.limberapp.backend.module.forms.client.formInstance

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep

class FormInstanceClientImpl(private val fetch: Fetch, private val json: Json) : FormInstanceClient {
  override suspend operator fun invoke(endpoint: FormInstanceApi.Post) =
    fetch(endpoint) { json.parse<FormInstanceRep.Complete>(it) }

  override suspend operator fun invoke(endpoint: FormInstanceApi.Get) =
    fetch(endpoint) { json.parse<FormInstanceRep.Complete>(it) }

  override suspend operator fun invoke(endpoint: FormInstanceApi.GetByFeatureGuid) =
    fetch(endpoint) { json.parseSet<FormInstanceRep.Summary>(it) }

  override suspend operator fun invoke(endpoint: FormInstanceApi.Delete) =
    fetch(endpoint)
}
