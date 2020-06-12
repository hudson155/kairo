package io.limberapp.web.context.globalState.action.formInstances

import io.limberapp.web.context.LoadableState

internal fun formInstancesReducer(
  state: FormInstancesState,
  action: FormInstancesAction
): FormInstancesState = with(state) {
  return@with when (action) {
    is FormInstancesAction.BeginLoading -> {
      plus(action.featureGuid to LoadableState.loading())
    }
    is FormInstancesAction.SetValue -> {
      plus(action.featureGuid to LoadableState.Loaded(action.formInstances.associateBy { it.guid }))
    }
    is FormInstancesAction.SetError -> {
      plus(action.featureGuid to LoadableState.Error(action.errorMessage))
    }
  }
}
