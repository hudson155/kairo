package io.limberapp.backend.module.forms.client.formInstance

import com.piperframework.util.Outcome
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep

interface FormInstanceClient {
  suspend operator fun invoke(endpoint: FormInstanceApi.Post): Outcome<FormInstanceRep.Complete>

  suspend operator fun invoke(endpoint: FormInstanceApi.Get): Outcome<FormInstanceRep.Complete>

  suspend operator fun invoke(endpoint: FormInstanceApi.GetByFeatureGuid): Outcome<List<FormInstanceRep.Summary>>

  suspend operator fun invoke(endpoint: FormInstanceApi.Delete): Outcome<Unit>
}
