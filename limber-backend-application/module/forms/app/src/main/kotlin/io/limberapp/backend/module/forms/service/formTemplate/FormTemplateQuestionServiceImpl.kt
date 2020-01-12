package io.limberapp.backend.module.forms.service.formTemplate

import com.google.inject.Inject
import com.piperframework.module.annotation.Store

internal class FormTemplateQuestionServiceImpl @Inject constructor(
    @Store private val formTemplateQuestionStore: FormTemplateQuestionService
) : FormTemplateQuestionService by formTemplateQuestionStore
