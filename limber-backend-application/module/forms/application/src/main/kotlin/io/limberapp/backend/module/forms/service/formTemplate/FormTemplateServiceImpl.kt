package io.limberapp.backend.module.forms.service.formTemplate

import com.google.inject.Inject
import io.limberapp.backend.module.forms.store.formTemplate.FormTemplateStore

internal class FormTemplateServiceImpl @Inject constructor(
    formTemplateStore: FormTemplateStore
) : FormTemplateService by formTemplateStore
