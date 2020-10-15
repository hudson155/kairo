package io.limberapp.backend.module.forms.client.formTemplate

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.client.LimberHttpClient
import io.limberapp.client.LimberHttpClientRequestBuilder

class FormTemplateClient @Inject constructor(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
      endpoint: FormTemplateApi.Post,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    readValue<FormTemplateRep.Complete>(checkNotNull(it))
  }

  suspend operator fun invoke(
      endpoint: FormTemplateApi.Get,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { readValue<FormTemplateRep.Complete>(it) }
  }

  suspend operator fun invoke(
      endpoint: FormTemplateApi.GetByFeatureGuid,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    readValue<Set<FormTemplateRep.Summary>>(checkNotNull(it))
  }

  suspend operator fun invoke(
      endpoint: FormTemplateApi.Patch,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { readValue<FormTemplateRep.Summary>(it) }
  }

  suspend operator fun invoke(
      endpoint: FormTemplateApi.Delete,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { readValue<Unit>(it) }
  }
}