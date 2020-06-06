package io.limberapp.web.context.globalState.action.formTemplates

import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.globalState.GlobalStateContext

internal fun formTemplatesReducer(
  state: GlobalStateContext,
  action: FormTemplatesAction
): GlobalStateContext = with(state.formTemplates) {
  return@with when (action) {
    is FormTemplatesAction.BeginLoading -> {
      state.copy(formTemplates = plus(action.featureGuid to LoadableState.loading()))
    }
    is FormTemplatesAction.SetValue -> {
      state.copy(
        formTemplates = plus(
          action.featureGuid to LoadableState.Loaded(action.formTemplates.associateBy { it.guid })
        )
      )
    }
    is FormTemplatesAction.SetError -> {
      state.copy(formTemplates = plus(action.featureGuid to LoadableState.Error(action.errorMessage)))
    }
  }
}
