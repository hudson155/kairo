package io.limberapp.backend.module.forms.service.formInstance

import com.google.inject.Inject
import io.limberapp.backend.module.forms.store.formInstance.FormInstanceQuestionStore

internal class FormInstanceQuestionServiceImpl @Inject constructor(
    formInstanceQuestionStore: FormInstanceQuestionStore
) : FormInstanceQuestionService by formInstanceQuestionStore
