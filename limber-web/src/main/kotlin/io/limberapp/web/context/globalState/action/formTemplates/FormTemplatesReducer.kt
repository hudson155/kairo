package io.limberapp.web.context.globalState.action.formTemplates

import io.limberapp.web.context.LoadableState

internal fun formTemplatesReducer(
  state: FormTemplatesState,
  action: FormTemplatesAction
): FormTemplatesState = with(state) {
  when (action) {
    is FormTemplateCompletesAction -> copy(completes = formTemplateCompletesReducer(completes, action))
    is FormTemplateSummariesAction -> copy(summaries = formTemplateSummariesReducer(summaries, action))
  }
}

private fun formTemplateCompletesReducer(
  state: FormTemplatesCompletesState,
  action: FormTemplateCompletesAction
): FormTemplatesCompletesState = with(state) {
  return@with when (action) {
    is FormTemplateCompletesAction.BeginLoading -> {
      plus(action.fromTemplateGuid to LoadableState.loading())
    }
    is FormTemplateCompletesAction.SetValue -> {
      plus(action.formTemplate.guid to LoadableState.Loaded(action.formTemplate))
    }
    is FormTemplateCompletesAction.SetError -> {
      plus(action.formTemplateGuid to LoadableState.Error(action.errorMessage))
    }
  }
}

private fun formTemplateSummariesReducer(
  state: FormTemplatesSummariesState,
  action: FormTemplateSummariesAction
): FormTemplatesSummariesState = with(state) {
  when (action) {
    is FormTemplateSummariesAction.BeginLoading -> {
      plus(action.featureGuid to LoadableState.loading())
    }
    is FormTemplateSummariesAction.SetValue -> {
      plus(action.featureGuid to LoadableState.Loaded(action.formTemplates.associateBy { it.guid }))
    }
    is FormTemplateSummariesAction.SetError -> {
      plus(action.featureGuid to LoadableState.Error(action.errorMessage))
    }
  }
}
