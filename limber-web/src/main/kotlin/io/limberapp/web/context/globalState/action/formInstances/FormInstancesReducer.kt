package io.limberapp.web.context.globalState.action.formInstances

import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.globalState.GlobalStateContext

internal fun formInstancesReducer(
  state: GlobalStateContext,
  action: FormInstancesAction
): GlobalStateContext = with(state.formInstances) {
  return@with when (action) {
    is FormInstancesAction.BeginLoading -> {
      state.copy(formInstances = plus(action.featureGuid to LoadableState.loading()))
    }
    is FormInstancesAction.SetValue -> {
      state.copy(
        formInstances = plus(
          action.featureGuid to LoadableState.Loaded(action.formInstances.associateBy { it.guid })
        )
      )
    }
    is FormInstancesAction.SetError -> {
      state.copy(formInstances = plus(action.featureGuid to LoadableState.Error(action.errorMessage)))
    }
  }
}
