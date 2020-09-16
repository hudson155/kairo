package io.limberapp.backend.module.forms.client.formTemplate

import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep

interface FormTemplateClient {
  suspend operator fun invoke(endpoint: FormTemplateApi.Post): Outcome<FormTemplateRep.Complete>

  suspend operator fun invoke(endpoint: FormTemplateApi.Get): Outcome<FormTemplateRep.Complete>

  suspend operator fun invoke(endpoint: FormTemplateApi.GetByFeatureGuid): Outcome<Set<FormTemplateRep.Summary>>

  suspend operator fun invoke(endpoint: FormTemplateApi.Patch): Outcome<FormTemplateRep.Summary>

  suspend operator fun invoke(endpoint: FormTemplateApi.Delete): Outcome<Unit>
}
