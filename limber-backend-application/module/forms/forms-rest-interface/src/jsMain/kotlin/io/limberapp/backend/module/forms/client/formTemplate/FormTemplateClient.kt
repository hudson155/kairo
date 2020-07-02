package io.limberapp.backend.module.forms.client.formTemplate

import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep

interface FormTemplateClient {
  suspend operator fun invoke(endpoint: FormTemplateApi.Post): Result<FormTemplateRep.Complete>

  suspend operator fun invoke(endpoint: FormTemplateApi.Get): Result<FormTemplateRep.Complete>

  suspend operator fun invoke(endpoint: FormTemplateApi.GetByFeatureGuid): Result<Set<FormTemplateRep.Summary>>

  suspend operator fun invoke(endpoint: FormTemplateApi.Patch): Result<FormTemplateRep.Summary>

  suspend operator fun invoke(endpoint: FormTemplateApi.Delete): Result<Unit>
}
