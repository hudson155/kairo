package io.limberapp.web.context.globalState.action.formTemplates

import com.piperframework.types.UUID
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.web.context.globalState.action.Action
import io.limberapp.web.util.ComponentWithApi
import io.limberapp.web.util.async
import react.*

internal abstract sealed class FormTemplatesAction : Action()

internal sealed class FormTemplateCompletesAction : FormTemplatesAction() {
  internal data class BeginLoading(val fromTemplateGuid: UUID) : FormTemplateCompletesAction()

  internal data class SetValue(
    val formTemplate: FormTemplateRep.Complete
  ) : FormTemplateCompletesAction()

  internal data class SetError(
    val formTemplateGuid: UUID,
    val errorMessage: String?
  ) : FormTemplateCompletesAction()
}

internal sealed class FormTemplateSummariesAction : FormTemplatesAction() {
  internal data class BeginLoading(val featureGuid: UUID) : FormTemplateSummariesAction()

  internal data class SetValue(
    val featureGuid: UUID,
    val formTemplates: Set<FormTemplateRep.Summary>
  ) : FormTemplateSummariesAction()

  internal data class SetError(
    val featureGuid: UUID,
    val errorMessage: String?
  ) : FormTemplateSummariesAction()
}

internal fun ComponentWithApi.loadFormTemplate(featureGuid: UUID, formTemplateGuid: UUID) {
  useEffect(listOf(formTemplateGuid)) {
    if (gs.formTemplates.completes[formTemplateGuid]?.hasBegunLoading == true) return@useEffect
    dispatch(FormTemplateCompletesAction.BeginLoading(formTemplateGuid))
    async {
      api.formTemplates(FormTemplateApi.Get(featureGuid, formTemplateGuid)).fold(
        onSuccess = { formTemplate -> dispatch(FormTemplateCompletesAction.SetValue(formTemplate)) },
        onFailure = { dispatch(FormTemplateCompletesAction.SetError(formTemplateGuid, it.message)) }
      )
    }
  }
}

internal fun ComponentWithApi.loadFormTemplates(featureGuid: UUID) {
  useEffect(listOf(featureGuid)) {
    if (gs.formTemplates.summaries[featureGuid]?.hasBegunLoading == true) return@useEffect
    dispatch(FormTemplateSummariesAction.BeginLoading(featureGuid))
    async {
      api.formTemplates(FormTemplateApi.GetByFeatureGuid(featureGuid)).fold(
        onSuccess = { formTemplates -> dispatch(FormTemplateSummariesAction.SetValue(featureGuid, formTemplates)) },
        onFailure = { dispatch(FormTemplateSummariesAction.SetError(featureGuid, it.message)) }
      )
    }
  }
}
