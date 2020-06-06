package io.limberapp.web.context.globalState.action.formTemplates

import io.limberapp.web.context.LoadableState
import io.limberapp.web.context.globalState.GlobalStateContext

internal fun formTemplatesReducer(
  state: GlobalStateContext,
  action: FormTemplatesAction
): GlobalStateContext = with(state.formTemplates) {
  return@with when (action) {
    is FormTemplatesAction.BeginLoading -> {
      check(get(action.featureGuid) == null)
      state.copy(formTemplates = plus(action.featureGuid to LoadableState.loading()))
    }
    is FormTemplatesAction.SetValue -> {
      val formTemplates = checkNotNull(get(action.featureGuid))
      check(formTemplates.isLoading)
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
