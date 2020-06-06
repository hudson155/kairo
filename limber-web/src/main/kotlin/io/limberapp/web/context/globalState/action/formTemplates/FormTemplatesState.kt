package io.limberapp.web.context.globalState.action.formTemplates

import com.piperframework.types.UUID
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.web.context.LoadableState

internal typealias FormTemplatesState = Map<UUID, LoadableState<Map<UUID, FormTemplateRep.Summary>>>
