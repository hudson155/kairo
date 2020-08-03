package io.limberapp.web.state.state.formInstances

import com.piperframework.types.UUID
import com.piperframework.util.Outcome
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep

internal interface FormInstancesMutator {
  suspend fun post(featureGuid: UUID, rep: FormInstanceRep.Creation): Outcome<UUID>
}
