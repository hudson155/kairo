package io.limberapp.backend.module.forms.service.formTemplate

import com.google.inject.Inject
import com.piperframework.module.annotation.Store

internal class FormTemplateServiceImpl @Inject constructor(
    @Store private val formTemplateStore: FormTemplateService
) : FormTemplateService by formTemplateStore
