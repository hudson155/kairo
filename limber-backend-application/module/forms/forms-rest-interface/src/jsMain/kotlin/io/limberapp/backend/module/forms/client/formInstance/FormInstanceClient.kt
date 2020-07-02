package io.limberapp.backend.module.forms.client.formInstance

import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep

interface FormInstanceClient {
  suspend operator fun invoke(endpoint: FormInstanceApi.Post): Result<FormInstanceRep.Complete>

  suspend operator fun invoke(endpoint: FormInstanceApi.GetByFeatureGuid): Result<Set<FormInstanceRep.Summary>>

  suspend operator fun invoke(endpoint: FormInstanceApi.Delete): Result<Unit>
}
