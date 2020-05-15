package io.limberapp.backend.module.forms.client.formInstance

import com.piperframework.restInterface.Fetch
import com.piperframework.serialization.Json
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.client.formInstance.question.FormInstanceQuestionClient
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep

class FormInstanceClient(private val fetch: Fetch, private val json: Json) {
  suspend operator fun invoke(endpoint: FormInstanceApi.Post): FormInstanceRep.Complete {
    val string = fetch(endpoint)
    return json.parse(string)
  }

  suspend operator fun invoke(endpoint: FormInstanceApi.GetByFeatureGuid): Set<FormInstanceRep.Summary> {
    val string = fetch(endpoint)
    return json.parseSet(string)
  }

  suspend operator fun invoke(endpoint: FormInstanceApi.Delete) {
    fetch(endpoint)
  }

  val questions = FormInstanceQuestionClient(fetch, json)
}
