package io.limberapp.backend.module.forms.service.formTemplate

import com.google.inject.Inject
import com.piperframework.module.annotation.Store

internal class FormTemplatePartServiceImpl @Inject constructor(
    @Store private val formTemplatePartStore: FormTemplatePartService
) : FormTemplatePartService by formTemplatePartStore
