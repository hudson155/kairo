package io.limberapp.web.context.globalState.action.formTemplates

import com.piperframework.types.UUID
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.web.context.LoadableState

private typealias TemplateGuid = UUID
private typealias FeatureGuid = UUID

internal typealias  FormTemplatesCompletesState = Map<TemplateGuid, LoadableState<FormTemplateRep.Complete>>
internal typealias  FormTemplatesSummariesState =
  Map<FeatureGuid, LoadableState<Map<TemplateGuid, FormTemplateRep.Summary>>>

internal data class FormTemplatesState(
  val completes: FormTemplatesCompletesState,
  val summaries: FormTemplatesSummariesState
) {
  companion object {
    fun initial() = FormTemplatesState(emptyMap(), emptyMap())
  }
}
