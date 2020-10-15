package io.limberapp.backend.module.forms.client.formInstance

import com.fasterxml.jackson.module.kotlin.readValue
import com.google.inject.Inject
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.client.LimberHttpClient
import io.limberapp.client.LimberHttpClientRequestBuilder

class FormInstanceClient @Inject constructor(private val httpClient: LimberHttpClient) {
  suspend operator fun invoke(
      endpoint: FormInstanceApi.Post,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    readValue<FormInstanceRep.Complete>(checkNotNull(it))
  }

  suspend operator fun invoke(
      endpoint: FormInstanceApi.Get,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { readValue<FormInstanceRep.Complete>(it) }
  }

  suspend operator fun invoke(
      endpoint: FormInstanceApi.GetByFeatureGuid,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    readValue<List<FormInstanceRep.Summary>>(checkNotNull(it))
  }

  suspend operator fun invoke(
      endpoint: FormInstanceApi.ExportByFeatureGuid,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    checkNotNull(it)
  }

  suspend operator fun invoke(
      endpoint: FormInstanceApi.Patch,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { readValue<FormInstanceRep.Summary>(it) }
  }

  suspend operator fun invoke(
      endpoint: FormInstanceApi.Delete,
      builder: LimberHttpClientRequestBuilder.() -> Unit = {},
  ) = httpClient.request(endpoint, builder) {
    it?.let { readValue<Unit>(it) }
  }
}
