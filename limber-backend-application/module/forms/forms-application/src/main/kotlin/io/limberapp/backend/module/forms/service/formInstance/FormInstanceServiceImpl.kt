package io.limberapp.backend.module.forms.service.formInstance

import com.google.inject.Inject
import io.limberapp.backend.module.forms.exception.formInstance.FormTemplateCannotBeInstantiatedInAnotherFeature
import io.limberapp.backend.module.forms.model.formInstance.FormInstanceModel
import io.limberapp.backend.module.forms.service.formTemplate.FormTemplateService
import io.limberapp.backend.module.forms.store.formInstance.FormInstanceStore
import java.util.UUID

internal class FormInstanceServiceImpl @Inject constructor(
    private val formTemplateService: FormTemplateService,
    private val formInstanceStore: FormInstanceStore
) : FormInstanceService {
    override fun create(model: FormInstanceModel) {
        if (model.featureGuid != formTemplateService.get(model.formTemplateGuid)?.featureGuid) {
            throw FormTemplateCannotBeInstantiatedInAnotherFeature()
        }
        formInstanceStore.create(model)
    }

    override fun get(formInstanceGuid: UUID) = formInstanceStore.get(formInstanceGuid)

    override fun getByFeatureGuid(featureGuid: UUID) = formInstanceStore.getByFeatureGuid(featureGuid)

    override fun delete(formInstanceGuid: UUID) = formInstanceStore.delete(formInstanceGuid)
}
