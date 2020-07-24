package io.limberapp.web.state.state.formTemplates

import com.piperframework.types.UUID
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep

private typealias FormTemplateGuid = UUID
internal typealias FormTemplatesState = Map<FormTemplateGuid, FormTemplateRep.Summary>
