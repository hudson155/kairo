package io.limberapp.backend.module.forms.client.formInstance

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.common.client.LimberHttpClient
import io.limberapp.common.client.LimberHttpClientRequestBuilder

class FormInstanceClient @Inject constructor(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
      endpoint: FormInstanceApi.Post,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): FormInstanceRep.Complete =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: FormInstanceApi.Get,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): FormInstanceRep.Complete? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: FormInstanceApi.GetByFeatureGuid,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): List<FormInstanceRep.Summary> =
      httpClient.request(endpoint, builder) { readValue(checkNotNull(it)) }

  suspend operator fun invoke(
      endpoint: FormInstanceApi.ExportByFeatureGuid,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): String =
      httpClient.request(endpoint, builder) { checkNotNull(it) }

  suspend operator fun invoke(
      endpoint: FormInstanceApi.Patch,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): FormInstanceRep.Summary? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }

  suspend operator fun invoke(
      endpoint: FormInstanceApi.Delete,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ): Unit? =
      httpClient.request(endpoint, builder) { it?.let { readValue(it) } }
}
