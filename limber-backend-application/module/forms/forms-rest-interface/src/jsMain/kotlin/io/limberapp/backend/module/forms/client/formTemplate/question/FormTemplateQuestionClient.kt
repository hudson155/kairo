package io.limberapp.backend.module.forms.client.formTemplate.question

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.forms.api.formTemplate.question.FormTemplateQuestionApi
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep

class FormTemplateQuestionClient(private val fetch: Fetch, private val json: Json) {
  suspend operator fun invoke(endpoint: FormTemplateQuestionApi.Post) =
    fetch(endpoint) { json.parse<FormTemplateQuestionRep.Complete>(it) }

  suspend operator fun invoke(endpoint: FormTemplateQuestionApi.Patch) =
    fetch(endpoint) { json.parse<FormTemplateQuestionRep.Complete>(it) }

  suspend operator fun invoke(endpoint: FormTemplateQuestionApi.Delete) =
    fetch(endpoint)
}
