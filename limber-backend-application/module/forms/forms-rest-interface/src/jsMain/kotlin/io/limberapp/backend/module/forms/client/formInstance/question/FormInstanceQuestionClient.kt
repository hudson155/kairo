package io.limberapp.backend.module.forms.client.formInstance.question

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.forms.api.formInstance.question.FormInstanceQuestionApi
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep

class FormInstanceQuestionClient(private val fetch: Fetch, private val json: Json) {
  suspend operator fun invoke(endpoint: FormInstanceQuestionApi.Put) =
    fetch(endpoint) { json.parse<FormInstanceQuestionRep.Complete>(it) }

  suspend operator fun invoke(endpoint: FormInstanceQuestionApi.Delete) =
    fetch(endpoint)
}
