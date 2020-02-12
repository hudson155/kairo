package io.limberapp.backend.module.forms.service.formTemplate

import com.google.inject.Inject
import io.limberapp.backend.module.forms.store.formTemplate.FormTemplateQuestionStore

internal class FormTemplateQuestionServiceImpl @Inject constructor(
    formTemplateQuestionStore: FormTemplateQuestionStore
) : FormTemplateQuestionService by formTemplateQuestionStore
