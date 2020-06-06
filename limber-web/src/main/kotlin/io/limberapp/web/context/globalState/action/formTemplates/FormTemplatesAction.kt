package io.limberapp.web.context.globalState.action.formTemplates

import com.piperframework.types.UUID
import io.limberapp.backend.module.forms.api.formTemplate.FormTemplateApi
import io.limberapp.backend.module.forms.rep.formTemplate.FormTemplateRep
import io.limberapp.web.context.globalState.action.Action
import io.limberapp.web.util.ComponentWithApi
import io.limberapp.web.util.async
import react.*

internal sealed class FormTemplatesAction : Action() {
  internal data class BeginLoading(val featureGuid: UUID) : FormTemplatesAction()

  internal data class SetValue(
    val featureGuid: UUID,
    val formTemplates: Set<FormTemplateRep.Summary>
  ) : FormTemplatesAction()

  internal data class SetError(
    val featureGuid: UUID,
    val errorMessage: String?
  ) : FormTemplatesAction()
}

internal fun ComponentWithApi.loadFormTemplates(featureGuid: UUID) {
  useEffect(listOf(featureGuid)) {
    if (gs.formTemplates[featureGuid]?.hasBegunLoading == true) return@useEffect
    dispatch(FormTemplatesAction.BeginLoading(featureGuid))
    async {
      api.formTemplates(FormTemplateApi.GetByFeatureGuid(featureGuid)).fold(
        onSuccess = { formTemplates -> dispatch(FormTemplatesAction.SetValue(featureGuid, formTemplates)) },
        onFailure = { dispatch(FormTemplatesAction.SetError(featureGuid, it.message)) }
      )
    }
  }
}
