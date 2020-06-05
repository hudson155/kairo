package io.limberapp.web.context.globalState.action.formInstances

import com.piperframework.types.UUID
import io.limberapp.backend.module.forms.api.formInstance.FormInstanceApi
import io.limberapp.backend.module.forms.rep.formInstance.FormInstanceRep
import io.limberapp.web.context.globalState.action.Action
import io.limberapp.web.util.ComponentWithApi
import io.limberapp.web.util.async
import react.*

internal sealed class FormInstancesAction : Action() {
  internal data class BeginLoading(val featureGuid: UUID) : FormInstancesAction()

  internal data class SetValue(
    val featureGuid: UUID,
    val formInstances: Set<FormInstanceRep.Summary>
  ) : FormInstancesAction()

  internal data class SetError(
    val featureGuid: UUID,
    val errorMessage: String?
  ) : FormInstancesAction()
}

internal fun ComponentWithApi.loadFormInstances(featureGuid: UUID) {
  useEffect(listOf(featureGuid)) {
    if (gs.formInstances[featureGuid]?.hasBegunLoading == true) return@useEffect
    dispatch(FormInstancesAction.BeginLoading(featureGuid))
    async {
      api.formInstances(FormInstanceApi.GetByFeatureGuid(featureGuid)).fold(
        onSuccess = { formInstances -> dispatch(FormInstancesAction.SetValue(featureGuid, formInstances)) },
        onFailure = { dispatch(FormInstancesAction.SetError(featureGuid, it.message)) }
      )
    }
  }
}
