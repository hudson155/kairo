package io.limberapp.web.context.globalState.action.formTemplates

import com.piperframework.types.UUID
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.web.context.globalState.action.Action
import io.limberapp.web.util.ComponentWithApi
import io.limberapp.web.util.async
import react.*

internal interface FormTemplatesAction

internal sealed class FormTemplateSummariesAction : Action(), FormTemplatesAction {
  internal data class Begin(val featureGuid: UUID) : FormTemplateSummariesAction()

  internal data class SetValue(
    val featureGuid: UUID,
    val formTemplates: Set<FormTemplateRep.Summary>
  ) : FormTemplateSummariesAction()

  internal data class SetError(
    val featureGuid: UUID,
    val errorMessage: String?
  ) : FormTemplateSummariesAction()
}

internal sealed class FormTemplateCompletesAction : Action(), FormTemplatesAction {
  internal data class Begin(val templateGuid: UUID) : FormTemplateCompletesAction()

  internal data class SetValue(
    val templateGuid: UUID,
    val formTemplate: FormTemplateRep.Complete
  ) : FormTemplateCompletesAction()

  internal data class SetError(
    val featureGuid: UUID,
    val errorMessage: String?
  ) : FormTemplateCompletesAction()
}

internal fun ComponentWithApi.loadFormTemplate(templateGuid: UUID) {
  useEffect(listOf(templateGuid)) {
    if (gs.formTemplates.completes[templateGuid]?.hasBegunLoading == true) return@useEffect
    dispatch(FormTemplateCompletesAction.Begin(templateGuid))
    async {
      api.formTemplates(FormTemplateApi.Get(templateGuid)).fold(
        onSuccess = { formTemplate -> dispatch(FormTemplateCompletesAction.SetValue(templateGuid, formTemplate)) },
        onFailure = { dispatch(FormTemplateCompletesAction.SetError(templateGuid, it.message)) }
      )
    }
  }
}

internal fun ComponentWithApi.loadFormTemplates(featureGuid: UUID) {
  useEffect(listOf(featureGuid)) {
    if (gs.formTemplates.summaries[featureGuid]?.hasBegunLoading == true) return@useEffect
    dispatch(FormTemplateSummariesAction.Begin(featureGuid))
    async {
      api.formTemplates(FormTemplateApi.GetByFeatureGuid(featureGuid)).fold(
        onSuccess = { formTemplates -> dispatch(FormTemplateSummariesAction.SetValue(featureGuid, formTemplates)) },
        onFailure = { dispatch(FormTemplateSummariesAction.SetError(featureGuid, it.message)) }
      )
    }
  }
}
