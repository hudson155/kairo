package io.limberapp.backend.module.forms.client.formTemplate

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.common.client.LimberHttpClient
import io.limberapp.common.client.LimberHttpClientRequestBuilder

class FormTemplateClient @Inject constructor(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
      endpoint: FormTemplateApi.Post,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): FormTemplateRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: FormTemplateApi.Get,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): FormTemplateRep.Complete? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: FormTemplateApi.GetByFeatureGuid,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): Set<FormTemplateRep.Summary> =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: FormTemplateApi.Patch,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): FormTemplateRep.Summary? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: FormTemplateApi.Delete,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): Unit? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }
}
