package io.limberapp.backend.module.forms.service.formInstance

import com.google.inject.Inject
import io.limberapp.backend.module.forms.exception.formInstance.FormTemplateCannotBeInstantiatedInAnotherFeature
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import io.limberapp.backend.module.forms.store.formInstance.FormInstanceStore

internal class FormInstanceServiceImpl @Inject constructor(
    private val formTemplateService: FormTemplateService,
    private val formInstanceStore: FormInstanceStore
) : FormInstanceService by formInstanceStore {

    override fun create(model: FormInstanceModel) {
        if (model.featureId != formTemplateService.get(model.formTemplateId)?.featureId) {
            throw FormTemplateCannotBeInstantiatedInAnotherFeature()
        }
        formInstanceStore.create(model)
    }
}
