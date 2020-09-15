package io.limberapp.web.state.state.formInstances

import io.limberapp.common.types.UUID
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep

internal interface FormInstancesMutator {
  suspend fun post(featureGuid: UUID, rep: FormInstanceRep.Creation): Outcome<UUID>
}
