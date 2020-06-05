package io.limberapp.web.context.globalState.action.formInstances

import com.piperframework.types.UUID
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.web.context.LoadableState

internal typealias FormInstancesState = Map<UUID, LoadableState<Map<UUID, FormInstanceRep.Summary>>>
