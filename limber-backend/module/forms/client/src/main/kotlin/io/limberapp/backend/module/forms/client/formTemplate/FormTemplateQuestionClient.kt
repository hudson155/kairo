package io.limberapp.backend.module.forms.client.formTemplate

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateQuestionApi
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateQuestionRep
import io.limberapp.common.client.LimberHttpClient
import io.limberapp.common.client.LimberHttpClientRequestBuilder

class FormTemplateQuestionClient @Inject constructor(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
      endpoint: FormTemplateQuestionApi.Post,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): FormTemplateQuestionRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: FormTemplateQuestionApi.Patch,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): FormTemplateQuestionRep.Complete? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: FormTemplateQuestionApi.Delete,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): Unit? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }
}
