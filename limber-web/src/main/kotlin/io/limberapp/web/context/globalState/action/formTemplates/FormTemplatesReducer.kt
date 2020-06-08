package io.limberapp.web.context.globalState.action.formTemplates

import io.limberapp.web.context.LoadableState

internal fun formTemplatesReducer(
  state: FormTemplatesState,
  action: FormTemplatesAction
): FormTemplatesState = with(state) {
  when (action) {
    is FormTemplateCompletesAction -> copy(completes = templateCompletesReducer(completes, action))
    is FormTemplateSummariesAction -> copy(summaries = templateSummariesReducer(summaries, action))
    else -> error("Unhandled action: $action.")
  }
}

private fun templateCompletesReducer(
  state: FormTemplatesCompletesState,
  action: FormTemplateCompletesAction
): FormTemplatesCompletesState = with(state) {
  return@with when (action) {
    is FormTemplateCompletesAction.Begin -> plus(action.templateGuid to LoadableState.loading())
    is FormTemplateCompletesAction.SetValue -> plus(action.templateGuid to LoadableState.Loaded(action.formTemplate))
    is FormTemplateCompletesAction.SetError -> plus(action.featureGuid to LoadableState.Error(action.errorMessage))
  }
}

private fun templateSummariesReducer(
  state: FormTemplatesSummariesState,
  action: FormTemplateSummariesAction
): FormTemplatesSummariesState = with(state) {
  when (action) {
    is FormTemplateSummariesAction.Begin -> plus(action.featureGuid to LoadableState.loading())
    is FormTemplateSummariesAction.SetValue -> plus(
      action.featureGuid to LoadableState.Loaded(action.formTemplates.associateBy { it.guid })
    )
    is FormTemplateSummariesAction.SetError -> plus(action.featureGuid to LoadableState.Error(action.errorMessage))
  }
}
