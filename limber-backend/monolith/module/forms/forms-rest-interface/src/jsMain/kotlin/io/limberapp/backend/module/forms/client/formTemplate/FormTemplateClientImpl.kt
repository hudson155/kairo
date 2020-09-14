package io.limberapp.backend.module.forms.client.formTemplate

import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.common.restInterface.Fetch
import io.limberapp.common.serialization.Json

class FormTemplateClientImpl(private val fetch: Fetch, private val json: Json) : FormTemplateClient {
  override suspend operator fun invoke(endpoint: FormTemplateApi.Post) =
    fetch(endpoint) { json.parse<FormTemplateRep.Complete>(it) }

  override suspend operator fun invoke(endpoint: FormTemplateApi.Get) =
    fetch(endpoint) { json.parse<FormTemplateRep.Complete>(it) }

  override suspend operator fun invoke(endpoint: FormTemplateApi.GetByFeatureGuid) =
    fetch(endpoint) { json.parseSet<FormTemplateRep.Summary>(it) }

  override suspend operator fun invoke(endpoint: FormTemplateApi.Patch) =
    fetch(endpoint) { json.parse<FormTemplateRep.Summary>(it) }

  override suspend operator fun invoke(endpoint: FormTemplateApi.Delete) =
    fetch(endpoint)
}
