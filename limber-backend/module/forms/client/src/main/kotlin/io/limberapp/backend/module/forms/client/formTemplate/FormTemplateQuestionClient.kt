package io.limberapp.backend.module.forms.client.formTemplate

import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateQuestionApi
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.client.LimberHttpClient
import io.limberapp.client.LimberHttpClientRequestBuilder

class FormTemplateQuestionClient(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
      endpoint: FormTemplateQuestionApi.Post,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    parse<FormTemplateQuestionRep.Complete>(checkNotNull(it))
  }

  suspend operator fun invoke(
      endpoint: FormTemplateQuestionApi.Patch,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { parse<FormTemplateQuestionRep.Complete>(it) }
  }

  suspend operator fun invoke(
      endpoint: FormTemplateQuestionApi.Delete,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { parse<Unit>(it) }
  }
}
