package io.limberapp.web.state.state.formInstances

import io.limberapp.common.types.UUID
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep

private typealias FormInstanceGuid = UUID
internal typealias FormInstancesState = Map<FormInstanceGuid, FormInstanceRep.Summary>
