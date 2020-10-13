package io.limberapp.backend.module.forms.client.formInstance

import io.limberapp.backend.module.forms.api.formInstance.FormInstanceQuestionApi
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceQuestionRep
import io.limberapp.client.LimberHttpClient
import io.limberapp.client.LimberHttpClientRequestBuilder

class FormInstanceQuestionClient(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
      endpoint: FormInstanceQuestionApi.Put,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    parse<FormInstanceQuestionRep.Complete>(checkNotNull(it))
  }

  suspend operator fun invoke(
      endpoint: FormInstanceQuestionApi.Delete,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { parse<Unit>(it) }
  }
}
