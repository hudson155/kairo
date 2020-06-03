package io.limberapp.backend.module.forms.client.formTemplate

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.client.formTemplate.question.FormTemplateQuestionClient
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep

class FormTemplateClient(private val fetch: Fetch, private val json: Json) {
  suspend operator fun invoke(endpoint: FormTemplateApi.Post) =
    fetch(endpoint) { json.parse<FormTemplateRep.Complete>(it) }

  suspend operator fun invoke(endpoint: FormTemplateApi.GetByFeatureGuid) =
    fetch(endpoint) { json.parseSet<FormTemplateRep.Summary>(it) }

  suspend operator fun invoke(endpoint: FormTemplateApi.Patch) =
    fetch(endpoint) { json.parse<FormTemplateRep.Summary>(it) }

  suspend operator fun invoke(endpoint: FormTemplateApi.Delete) =
    fetch(endpoint)

  val questions = FormTemplateQuestionClient(fetch, json)
}
