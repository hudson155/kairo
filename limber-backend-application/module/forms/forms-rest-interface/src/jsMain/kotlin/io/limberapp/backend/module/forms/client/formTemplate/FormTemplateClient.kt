package io.limberapp.backend.module.forms.client.formTemplate

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.client.formTemplate.question.FormTemplateQuestionClient
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep

class FormTemplateClient(private val fetch: Fetch, private val json: Json) {
  suspend operator fun invoke(endpoint: FormTemplateApi.Post): FormTemplateRep.Complete {
    val string = fetch(endpoint).getOrThrow()
    return json.parse(string)
  }

  suspend operator fun invoke(endpoint: FormTemplateApi.GetByFeatureGuid): Set<FormTemplateRep.Summary> {
    val string = fetch(endpoint).getOrThrow()
    return json.parseSet(string)
  }

  suspend operator fun invoke(endpoint: FormTemplateApi.Patch): FormTemplateRep.Summary {
    val string = fetch(endpoint).getOrThrow()
    return json.parse(string)
  }

  suspend operator fun invoke(endpoint: FormTemplateApi.Delete) {
    fetch(endpoint)
  }

  val questions = FormTemplateQuestionClient(fetch, json)
}
