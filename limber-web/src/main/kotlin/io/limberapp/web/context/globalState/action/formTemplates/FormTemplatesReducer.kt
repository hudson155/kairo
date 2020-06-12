package io.limberapp.web.context.globalState.action.formTemplates

import io.limberapp.web.context.LoadableState

internal fun formTemplatesReducer(
  state: FormTemplatesState,
  action: FormTemplatesAction
): FormTemplatesState = with(state) {
  return@with when (action) {
    is FormTemplatesAction.BeginLoading -> {
      plus(action.featureGuid to LoadableState.loading())
    }
    is FormTemplatesAction.SetValue -> {
      plus(action.featureGuid to LoadableState.Loaded(action.formTemplates.associateBy { it.guid }))
    }
    is FormTemplatesAction.SetError -> {
      plus(action.featureGuid to LoadableState.Error(action.errorMessage))
    }
  }
}
