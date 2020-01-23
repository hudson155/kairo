package io.limberapp.backend.module.forms.service.formInstance

import com.google.inject.Inject
import io.limberapp.backend.module.forms.store.formInstance.FormInstanceStore

internal class FormInstanceServiceImpl @Inject constructor(
    private val formInstanceStore: FormInstanceStore
) : FormInstanceService by formInstanceStore
