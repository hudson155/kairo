package io.limberapp.backend.module.forms.client.formInstance

import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.common.restInterface.Fetch
import io.limberapp.common.serialization.Json

class FormInstanceClientImpl(private val fetch: Fetch, private val json: Json) : FormInstanceClient {
  override suspend operator fun invoke(endpoint: FormInstanceApi.Post) =
    fetch(endpoint) { json.parse<FormInstanceRep.Complete>(it) }

  override suspend operator fun invoke(endpoint: FormInstanceApi.Get) =
    fetch(endpoint) { json.parse<FormInstanceRep.Complete>(it) }

  override suspend operator fun invoke(endpoint: FormInstanceApi.GetByFeatureGuid) =
    fetch(endpoint) { json.parseList<FormInstanceRep.Summary>(it) }

  override suspend operator fun invoke(endpoint: FormInstanceApi.Patch) =
    fetch(endpoint) { json.parse<FormInstanceRep.Complete>(it) }

  override suspend operator fun invoke(endpoint: FormInstanceApi.Delete) =
    fetch(endpoint)
}
