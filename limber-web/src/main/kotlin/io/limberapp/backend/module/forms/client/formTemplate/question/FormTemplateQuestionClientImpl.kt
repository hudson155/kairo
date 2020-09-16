package io.limberapp.backend.module.forms.client.formTemplate.question

import io.limberapp.backend.module.forms.api.formTemplate.question.FormTemplateQuestionApi
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.common.restInterface.Fetch
import io.limberapp.common.serialization.Json

class FormTemplateQuestionClientImpl(private val fetch: Fetch, private val json: Json) : FormTemplateQuestionClient {
  override suspend operator fun invoke(endpoint: FormTemplateQuestionApi.Post) =
    fetch(endpoint) { json.parse<FormTemplateQuestionRep.Complete>(it) }

  override suspend operator fun invoke(endpoint: FormTemplateQuestionApi.Patch) =
    fetch(endpoint) { json.parse<FormTemplateQuestionRep.Complete>(it) }

  override suspend operator fun invoke(endpoint: FormTemplateQuestionApi.Delete) =
    fetch(endpoint)
}
